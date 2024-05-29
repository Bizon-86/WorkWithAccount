package ru.inno.local.cache;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
public class ParamsWithMethod {
        private Method method;
        private Object[] params;


        public ParamsWithMethod( Method method, Object[] params) {
            this.method = method;
            this.params = params;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamsWithMethod)) return false;
        ParamsWithMethod that = (ParamsWithMethod) o;
        return Objects.equals(method, that.method) && Arrays.equals(params, that.params);
    }

    @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(params);
            return result;
        }
    }

