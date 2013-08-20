package dk.schioler.economy.in;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


public class CategoryMatcherPropertiesSrc implements AccountMatcher {
   private static final Logger LOG = Logger.getLogger(CategoryMatcherPropertiesSrc.class);
   private Properties categories;
   Properties properties = new Properties();

   private final static String matcherFile = "matchpattern-to-category-map.properties";

   public CategoryMatcherPropertiesSrc() {
      this(matcherFile);
   }

   public CategoryMatcherPropertiesSrc(String file) {
      super();
      LOG.debug("Constrctor received file=" + file);
      // load file and read all patterns.
      InputStream resourceAsStream = CategoryMatcherPropertiesSrc.class.getResourceAsStream("/" + file);
      categories = new Properties();
      try {
         categories.load(resourceAsStream);
         Set<Entry<Object, Object>> entrySet = categories.entrySet();
         LOG.debug(entrySet);
         for (Entry<Object, Object> entry : entrySet) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (value != null && !("".equals(value))) {
               String[] split = value.split(",");
               for (int i = 0; i < split.length; i++) {
                  if (split[i] != null && !("".equals(split[i]))) {
                     properties.setProperty(split[i], key);
                  }
               }
            }
         }
         LOG.debug(properties.toString());
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         try {
            if (resourceAsStream != null)
               resourceAsStream.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   }

   public String[] getCategory(String text) {
      String textUpper = text.toUpperCase();
      // LOG.debug(textUpper);
      String category = null;
      Set<Entry<Object, Object>> entrySet = properties.entrySet();
      for (Entry<Object, Object> entry : entrySet) {
         String key = (String) entry.getKey();
         if (textUpper.contains(key.toUpperCase())) {
            LOG.debug("match: key=" + key + ": line=" + textUpper);
            category = (String) entry.getValue();
            break;
         }
      }
      // LOG.debug("Found category=" + category);
      if (category == null) {
         return null;
      } else {
         return category.split("\\.");
      }
   }

   public long matchText(String text) {
      // TODO Auto-generated method stub
      return 0;
   }

//   public CategoryGroupAccount matchText(String text) {
//      String textUpper = text.toUpperCase();
//      // LOG.debug(textUpper);
//      CategoryGroupAccount retVal = null;
//      String category = null;
//      Set<Entry<Object, Object>> entrySet = properties.entrySet();
//      for (Entry<Object, Object> entry : entrySet) {
//         String key = (String) entry.getKey();
//         if (textUpper.contains(key.toUpperCase())) {
//            LOG.debug("match: key=" + key + ": line=" + textUpper);
//            category = (String) entry.getValue();
//            break;
//         }
//      }
      // LOG.debug("Found category=" + category);
//      if (category != null) {
//         String[] split = category.split("\\.");
//         retVal = new CategoryGroupAccount(Short.parseShort(split[0]), Short.parseShort(split[1]), Short.parseShort(split[2]));
//      }
//      return retVal;
//   }
}
