package com.redsponge.sponge.animation;

import com.badlogic.gdx.utils.JsonValue;
import com.redsponge.sponge.animation.AnimationChangeCondition.AndChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.EqualsComparisonChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.GreaterThanComparisonChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.GreaterThanEqualsComparisonChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.LesserThanEqualsComparisonChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.LesserThanComparisonChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.NotChangeCondition;
import com.redsponge.sponge.animation.AnimationChangeCondition.OrChangeCondition;
import com.redsponge.sponge.animation.Values.ConstantValueHolder;
import com.redsponge.sponge.animation.Values.ValueHolder;

import java.util.HashMap;

public class AnimationNode {

    private String name;
    private HashMap<AnimationChangeCondition, String> changes;


    public AnimationNode(String name) {
        this.name = name;
        this.changes = new HashMap<>();
    }

    public AnimationNode(JsonValue json, AnimationNodeSystem system, HashMap<String, AnimationNode> prefabs) {
        this(json.name());
        if(json.has("links")) {
            for (JsonValue link : json.get("links")) {
                parseLink(link, system, prefabs);
            }
        }
    }

    private void parseLink(JsonValue link, AnimationNodeSystem system, HashMap<String, AnimationNode> prefabs) {
        if(link.has("prefab")) {
            AnimationNode prefab = prefabs.get(link.getString("prefab"));
            if(prefab == null) {
                throw new RuntimeException("Unknown prefab '" + link.getString("prefab") + "'!");
            }
            prefab.changes.forEach((a, b) -> {
                System.out.println(a + " " + b);
                changes.put(a, b);
            });
        } else {
            String to = link.getString("to");
            AnimationChangeCondition condition = parseCondition(link.get("condition"), system);
            changes.put(condition, to);
        }
    }

    private AnimationChangeCondition parseCondition(JsonValue condition, AnimationNodeSystem system) {
        String type = condition.getString("type");
        switch (type) {
            case "equals": {
                ValueHolder a = parseValue(condition.get("var_a"), system);
                ValueHolder b = parseValue(condition.get("var_b"), system);
                return new EqualsComparisonChangeCondition(a, b);
            }
            case "greater_than": {
                ValueHolder a = parseValue(condition.get("var_a"), system);
                ValueHolder b = parseValue(condition.get("var_b"), system);
                return new GreaterThanComparisonChangeCondition(a, b);
            }
            case "greater_than_equals": {
                ValueHolder a = parseValue(condition.get("var_a"), system);
                ValueHolder b = parseValue(condition.get("var_b"), system);
                return new GreaterThanEqualsComparisonChangeCondition(a, b);
            }
            case "lesser_than": {
                ValueHolder a = parseValue(condition.get("var_a"), system);
                ValueHolder b = parseValue(condition.get("var_b"), system);
                return new LesserThanComparisonChangeCondition(a, b);
            }
            case "lesser_than_equals": {
                ValueHolder a = parseValue(condition.get("var_a"), system);
                ValueHolder b = parseValue(condition.get("var_b"), system);
                return new LesserThanEqualsComparisonChangeCondition(a, b);
            }
            case "not": {
                return new NotChangeCondition(parseCondition(condition.get("condition"), system));
            }
            case "and": {
                AnimationChangeCondition a = parseCondition(condition.get("condition_a"), system);
                AnimationChangeCondition b = parseCondition(condition.get("condition_b"), system);
                return new AndChangeCondition(a, b);
            }
            case "or": {
                AnimationChangeCondition a = parseCondition(condition.get("condition_a"), system);
                AnimationChangeCondition b = parseCondition(condition.get("condition_b"), system);
                return new OrChangeCondition(a, b);
            }
        }
        throw new RuntimeException("Failed to parse condition of type '" + type + "'\n json is\n" + condition);
    }

    private ValueHolder parseValue(JsonValue value, AnimationNodeSystem system) {
        String type = value.getString("type");
        switch (type) {
            case "variable":
                return system.getHolder(value.getString("name"));
            case "boolean":
                return new ConstantValueHolder<>(value.getBoolean("value"));
            case "float":
                return new ConstantValueHolder<>(value.getFloat("value"));
            case "int":
                return new ConstantValueHolder<>(value.getInt("value"));
        }
        throw new RuntimeException("Failed to parse value of type '" + type + "'\n json is\n" + value);
    }

    public String testChanges() {
        for (AnimationChangeCondition animationChangeCondition : changes.keySet()) {
            if(animationChangeCondition.test()) return changes.get(animationChangeCondition);
        }
        return null;
    }

    public void addCondition(String to, AnimationChangeCondition condition) {
        changes.put(condition, to);
    }
}
