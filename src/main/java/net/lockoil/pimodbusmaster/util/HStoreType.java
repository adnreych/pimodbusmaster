package net.lockoil.pimodbusmaster.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.postgresql.util.HStoreConverter;
import org.postgresql.util.PGobject;

/**
 * Тип hstore
 */
public class HStoreType extends AbstractType {

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.OTHER };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Map> returnedClass() {
		return Map.class;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		if(names != null && names.length > 0 && rs != null) {
			return HStoreConverter.fromString(rs.getString(names[0]));
		}
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
        if(value != null && st != null) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) value;
			PGobject pgo = new PGobject();
			pgo.setType("hstore");
			pgo.setValue(HStoreConverter.toString(map));
			st.setObject(index, pgo);
        }
	}
}
