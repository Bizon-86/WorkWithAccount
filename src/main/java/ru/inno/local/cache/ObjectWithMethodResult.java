package ru.inno.local.cache;

public class ObjectWithMethodResult {
        private Object object;
        private Object methodResult;
        private ParamsWithMethod paramsWithMethod;
        private long lastUse;

        public ObjectWithMethodResult(Object object, Object methodResult, ParamsWithMethod paramsWithMethod, long lastUse) {
            this.object = object;
            this.methodResult = methodResult;
            this.paramsWithMethod = paramsWithMethod;
            this.lastUse = lastUse;
        }

        public Object getObject() {
            return object;
        }

        public Object getMethodResult() {
            return methodResult;
        }

        public ParamsWithMethod getParamsWithMethod() {
            return paramsWithMethod;
        }
        public long getLastUse() {
            return lastUse;
        }

        public void setLastUse(long lastUse) {
            this.lastUse = lastUse;
        }
    }

