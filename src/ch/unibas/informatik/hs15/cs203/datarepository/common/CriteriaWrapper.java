package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;

/**
 * The {@link Criteria} wrapper. Since the api classes must not be changed, this
 * wrapper class provides extended access to the {@link Criteria} object.
 *
 * @author Loris
 * @see Criteria
 */
public class CriteriaWrapper {

	/**
	 * Creates a CriteriaWrapper object with all values set to <tt>null</tt>
	 *
	 * @return A CriteriaWrapper object to retain all meta data.
	 * @see Criteria#all()
	 */
	public static CriteriaWrapper all() {
		return new CriteriaWrapper(Criteria.all());
	}

	/**
	 * Creates a CriteriaWrapper object for the given ID.
	 *
	 * @param id
	 *            The ID, a not null, non-empty string.
	 * @return A CriteriaWrapper object for the given ID.
	 * @see Criteria#forId(String)
	 */
	public static CriteriaWrapper forId(final String id) {
		return new CriteriaWrapper(Criteria.forId(id));
	}

	/**
	 * Wrapped object
	 */
	private final Criteria wrapped;

	/**
	 * Creates a CriteriaWrapper for the given Criteria.
	 *
	 * @param toWrapp
	 *            The Criteria Object to wrap
	 * @see Criteria
	 */
	public CriteriaWrapper(final Criteria toWrapp) {
		wrapped = toWrapp;
	}

	/**
	 * Creates a CirteriaWrapper object for the given ID.
	 *
	 * @param id
	 *            The ID, a not null, non-empty string.
	 * @see Criteria#forId(String)
	 */
	public CriteriaWrapper(final String id) {
		this(Criteria.forId(id));
	}

	/**
	 * Creates a new CriteriaWrapper object with the given arguments.
	 *
	 * @param nameOrNull
	 *            The name of the data set to match
	 * @param textOrNull
	 *            A text snippet which the data set's name or description must
	 *            contain
	 * @param afterOrNull
	 *            The time stamp of all matching data sets has to be after this
	 *            time stamp.
	 * @param beforeOrNull
	 *            The time stamp of all matching data sets has to be before this
	 *            time stamp.
	 * @see Criteria#Criteria(String, String, Date, Date)
	 */
	public CriteriaWrapper(final String nameOrNull, final String textOrNull,
			final Date afterOrNull, final Date beforeOrNull) {
		this(new Criteria(nameOrNull, textOrNull, afterOrNull, beforeOrNull));
	}

	/**
	 * Compares this object and another one on equality. Eclipse generated.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (isThis(obj)) {
			return true;
		} else if (isNull(obj)) {
			return false;
		} else {
			if (isInstanceOf(obj)) {
				final CriteriaWrapper o = (CriteriaWrapper) obj;
				boolean after = isEqual(this.getAfter(), o.getAfter() );
				boolean before = isEqual(this.getBefore(), o.getBefore() );
				boolean id = isEqual(this.getId(), o.getId() );
				boolean name = isEqual(this.getName(), o.getName() );
				boolean text = isEqual(this.getText(), o.getText() );
				return after && before && id && name && text;
			}
			return false;
		}
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getAfter()
	 */
	public Date getAfter() {
		return wrapped.getAfter();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getBefore()
	 */
	public Date getBefore() {
		return wrapped.getBefore();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getId()
	 */
	public String getId() {
		return wrapped.getId();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getName()
	 */
	public String getName() {
		return wrapped.getName();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getText()
	 */
	public String getText() {
		return wrapped.getText();
	}

	public Criteria getWrappedObject() {
		return wrapped;
	}

	/**
	 * Returns a hash value for this object. The hash value method is eclipse
	 * generated.
	 *
	 * @return A hash value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getAfter() == null) ? 0 : getAfter().hashCode());
		result = prime * result
				+ ((getBefore() == null) ? 0 : getBefore().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result
				+ ((getText() == null) ? 0 : getText().hashCode());
		return result;
	}

	public boolean isNull() {
		return wrapped == null;
	}

	/**
	 * Returns true if this is a criteria query for a certain id. In other
	 * words: returns true if and only if <tt>getId() != null</tt> and all other
	 * getters return <tt>null</tt>
	 *
	 * @return
	 */
	public boolean onlyID() {
		return getId() != null && (getAfter() == null && getBefore() == null
				&& getName() == null && getText() == null);
	}
	
	public boolean matches(MetaDataWrapper meta){
		if(meta == null){
			return false;
		}
		boolean out = true;
		if(getAfter() != null){
			out = out && isAfter(meta);
		}
		if(getBefore() != null){
			out = out && isBefore(meta);
		}
		if(getName() != null){
			out = out && matchesName(meta);
		}
		if(getText() != null){
			out = out && containsSnippet(meta);
		}
		return out;
	}
	
	private boolean isAfter(MetaDataWrapper meta){
		return getAfter().compareTo(meta.getTimestamp()) < 0;
	}
	private boolean isBefore(MetaDataWrapper meta){
		return getBefore().compareTo(meta.getTimestamp()) > 0;
	}
	private boolean containsSnippet(MetaDataWrapper meta){
		return meta.getName().contains(getText()) || meta.getDescription().contains(getText());
	}
	private boolean matchesName(MetaDataWrapper meta){
		return getName().equals(meta.getName());
	}
	
	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CriteriaWrapper [");
		if (getId() != null) {
			builder.append("getId()=");
			builder.append(getId());
			builder.append(", ");
		}
		if (getName() != null) {
			builder.append("getName()=");
			builder.append(getName());
			builder.append(", ");
		}
		if (getText() != null) {
			builder.append("getText()=");
			builder.append(getText());
			builder.append(", ");
		}
		if (getBefore() != null) {
			builder.append("getBefore()=");
			builder.append(getBefore());
			builder.append(", ");
		}
		if (getAfter() != null) {
			builder.append("getAfter()=");
			builder.append(getAfter());
		}
		builder.append("]");
		return builder.toString();
	}

	private boolean isEqual(Object o1, Object o2){
		boolean out = false;
		if(o1 == null){
			out = o2 == null;
		}else{
			out = o1.equals(o2);
		}
		return out;
	}
	
	
	private boolean isInstanceOf(final Object obj) {
		return obj instanceof CriteriaWrapper;
	}

	private boolean isNull(final Object obj) {
		return obj == null;
	}

	private boolean isThis(final Object obj) {
		return this == obj;
	}
}
