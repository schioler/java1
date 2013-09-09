package dk.schioler.economy.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

   /**
	 *
	 */
   private static final long serialVersionUID = 1L;

   private final Long id;
   private final String name;
   private final Date timestamp;

   public User(Long id, String name, Date timestamp) {
      super();
      this.id = id;
      this.name = name;
      this.timestamp = timestamp;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   @Override
   public String toString() {
      return "User [id=" + id + ", name=" + name + ", timestamp=" + timestamp + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      User other = (User) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (timestamp == null) {
         if (other.timestamp != null)
            return false;
      } else if (!timestamp.equals(other.timestamp))
         return false;
      return true;
   }

}
