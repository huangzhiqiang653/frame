package com.zx.common.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


public abstract class ValidateUtil {
	
	/** Check whether string s is null */
	public static boolean isNull(Object s) {
		return (s == null);
	}

	/** Check whether string s is NOT null */
	public static boolean isNotNull(Object s) {
		return (s != null);
	}

	/** Check whether string s is null */
	public static boolean isNull(String s) {
		return (s == null);
	}

	/** Check whether string s is NOT null */
	public static boolean isNotNull(String s) {
		return (s != null);
	}

	/** Check whether strings is empty. */
	public static boolean isEmpty(String s) {
		return ((s == null) || (s.length() == 0));
	}

	/** Check whether collection c is empty. */
	public static boolean isEmpty(Collection<?> c) {
		return ((c == null) || (c.size() == 0));
	}

	/** Check whether string s is NOT empty. */
	public static boolean isNotEmpty(String s) {
		return ((s != null) && (s.length() > 0));
	}

	/** Check whether collection c is NOT empty. */
	public static boolean isNotEmpty(Collection<?> c) {
		return ((c != null) && (c.size() > 0));
	}

	/** Check whether Object c is a String. */
	public static boolean isString(Object obj) {
		return ((obj != null) && (obj instanceof String));
	}
	
	/** Check whether Map map is empty. */
	public static boolean isEmpty(Map<?,?> map) {
        return ((map == null) || map.isEmpty());
    }

	/** Check whether String[] s is empty */
	public static boolean isEmpty(String[] s) {
		return ((s == null) || (s.length == 0));
	}

	/** Check whether String[] s is NOT empty */
	public static boolean isNotEmpty(String[] s) {
		return ((s != null) && (s.length > 0));
	}
	
	/** Check whether a trimed String s is empty */
    public static boolean isTrimEmpty(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

	/**
	 * Returns true if all characters are correct email format
	 */
	public static boolean isEmail(String email) {
		return email != null
				&& email
						.matches("(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)");
	}

	/**
	 * Return true if all characters are correct URL format
	 */
	public static boolean isUrl(String url) {
		return url != null
				&& url.matches("http://([w-]+.)+[w-]+(/[w- ./?%&=]*)?");
	}
	
	/** Check whether Array array is empty */
    public static boolean isEmptyArray( Object array ) {
        if( null == array )
            return true;
        if( array.getClass().isArray() )
            return Array.getLength( array ) == 0;
        return false;
    }
}
