package com.redsponge.sponge.animation;

import com.redsponge.sponge.animation.Values.ValueHolder;

import java.util.ArrayList;
import java.util.List;

public interface AnimationChangeCondition {
    boolean test();
    static AnimationChangeConditionBuilder builder() {
        return new AnimationChangeConditionBuilder();
    }

    class NotChangeCondition implements AnimationChangeCondition{
        AnimationChangeCondition condition;

        public NotChangeCondition(AnimationChangeCondition condition) {
            this.condition = condition;
        }

        @Override
        public boolean test() {
            return !condition.test();
        }
    }

    class AndChangeCondition implements AnimationChangeCondition {

        private AnimationChangeCondition a, b;

        public AndChangeCondition(AnimationChangeCondition a, AnimationChangeCondition b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean test() {
            return a.test() && b.test();
        }
    }

    class OrChangeCondition implements AnimationChangeCondition {
        private AnimationChangeCondition a, b;

        public OrChangeCondition(AnimationChangeCondition a, AnimationChangeCondition b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean test() {
            return a.test() || b.test();
        }
    }

    abstract class ComparisonChangeCondition<T extends Comparable> implements AnimationChangeCondition {
        ValueHolder<T> a, b;

        public ComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            this.a = a;
            this.b = b;
        }

        public abstract boolean compare();

        @Override
        public final boolean test() {
            return compare();
        }
    }

    class GreaterThanComparisonChangeCondition<T extends Comparable> extends ComparisonChangeCondition<T> {
        public GreaterThanComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            super(a, b);
        }

        @Override
        public boolean compare() {
            return a.get().compareTo(b.get()) > 0;
        }
    }

    class GreaterThanEqualsComparisonChangeCondition<T extends Comparable> extends ComparisonChangeCondition<T> {
        public GreaterThanEqualsComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            super(a, b);
        }

        @Override
        public boolean compare() {
            return a.get().compareTo(b.get()) >= 0;
        }
    }

    class EqualsComparisonChangeCondition<T extends Comparable> extends ComparisonChangeCondition<T> {
        public EqualsComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            super(a, b);
        }

        @Override
        public boolean compare() {
            return a.get().compareTo(b.get()) == 0;
        }
    }

    class LesserThanEqualsComparisonChangeCondition<T extends Comparable> extends ComparisonChangeCondition<T> {
        public LesserThanEqualsComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            super(a, b);
        }

        @Override
        public boolean compare() {
            return a.get().compareTo(b.get()) <= 0;
        }
    }

    class LesserThanComparisonChangeCondition<T extends Comparable> extends ComparisonChangeCondition<T> {
        public LesserThanComparisonChangeCondition(ValueHolder<T> a, ValueHolder<T> b) {
            super(a, b);
        }

        @Override
        public boolean compare() {
            return a.get().compareTo(b.get()) < 0;
        }
    }

    class AnimationChangeConditionBuilder {
        private List<List<AnimationChangeCondition>> conditions;

        private List<AnimationChangeCondition> currentConditions;

        public AnimationChangeConditionBuilder() {
            conditions = new ArrayList<>();
            currentConditions = new ArrayList<>();
        }

        public AnimationChangeConditionBuilder and(AnimationChangeCondition condition) {
            currentConditions.add(condition);
            return this;
        }

        public AnimationChangeConditionBuilder or(AnimationChangeCondition condition) {
            conditions.add(currentConditions);
            currentConditions = new ArrayList<>();
            return and(condition);
        }

        public AnimationChangeCondition build() {
            if(conditions.size() == 0) return null;
            if(conditions.size() == 1) {
                return buildList(conditions.get(0));
            }

            OrChangeCondition output;
            AnimationChangeCondition a = buildList(conditions.get(0));
            AnimationChangeCondition b = buildList(conditions.get(1));
            output = new OrChangeCondition(a, b);
            for(int i = 2; i < conditions.size(); i++) {
                output = new OrChangeCondition(output, buildList(conditions.get(i)));
            }
            return output;
        }

        private AnimationChangeCondition buildList(List<AnimationChangeCondition> animationChangeConditions) {
            if(animationChangeConditions.size() == 0) return null;
            if(animationChangeConditions.size() == 1) {
                return animationChangeConditions.get(0);
            }
            AndChangeCondition output = new AndChangeCondition(animationChangeConditions.get(0), animationChangeConditions.get(1));
            for (int i = 2; i < animationChangeConditions.size(); i++) {
                output = new AndChangeCondition(output, animationChangeConditions.get(i));
            }
            return output;
        }
    }
}
