package zzz404.safesql;

import org.apache.commons.lang3.builder.EqualsBuilder;

public interface EqualsSupport {
    public Object[] equalsByValues();

    public static boolean isEquals(EqualsSupport o1, Object o2) {
        if (o1.getClass() != o2.getClass()) {
            return false;
        }
        else {
            Object[] os1 = ((EqualsSupport) o1).equalsByValues();
            if (os1 == null) {
                return true;
            }
            Object[] os2 = ((EqualsSupport) o2).equalsByValues();
            EqualsBuilder builder = new EqualsBuilder();
            for (int i = 0; i < os1.length; i++) {
                builder.append(os1[i], os2[i]);
            }
            return builder.isEquals();
        }
    }

}
