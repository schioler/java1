package dk.schioler.economy.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dk.schioler.economy.util.FileUtil;

@Component("doAccountInsert")
public class DoAccountAndPatternInsert {
   private static final Logger LOG = Logger.getLogger(DoAccountAndPatternInsert.class);
   @Autowired
   JdbcTemplate jdbcTemplate;

   private void deleteAll() {
      String delete = "delete from ACCOUNT";
      jdbcTemplate.update(delete);
   }

   public void doAccountInsert(String accountFile) {
      deleteAll();

      BufferedReader br = FileUtil.openFile(accountFile);
      try {
         String line = br.readLine();
         int id = 0;
         int categoryId = 0;
         int groupId = 0;
         int accountId = 0;
         while (line != null) {
            String[] splitted = line.split(",");
            LOG.debug(Arrays.toString(splitted));
            String type = isAccountLine(splitted);
            if (type != null) {
               id++;
               if (CATEGORY.equals(type)) {
                  categoryId = id;
                  createCategory(categoryId, splitted);
               } else if (GROUP.equals(type)) {
                  groupId = id;
                  createGroup(categoryId, groupId, splitted);
               } else if (ACCOUNT.equals(type)) {
                  accountId = id;
                  createAccount(groupId, accountId, splitted);
               }
            }

            line = br.readLine();
         }
      } catch (IOException e) {

         e.printStackTrace();
      } finally {
         FileUtil.closeReader(br);
      }
   }

   static final String INSERT_ACCOUNT_SQL = "insert into ACCOUNT (id, parent_id,type, name) values (?,?,?,?)";
   static final String INSERT_MATCH_SQL = "insert into PATTERN_MATCH (account_id,pattern) values (?,?)";

   private void createCategory(int categoryId, String[] line) {
      jdbcTemplate.update(INSERT_ACCOUNT_SQL, categoryId, null, CATEGORY, line[3]);
   }

   private void createGroup(int categoryId, int groupId, String[] line) {
      jdbcTemplate.update(INSERT_ACCOUNT_SQL, groupId, categoryId, GROUP, line[3]);
   }

   private void createAccount(int groupId, int accountId, String[] line) {
      jdbcTemplate.update(INSERT_ACCOUNT_SQL, accountId, groupId, ACCOUNT, line[3]);
      if (line.length > 3) {
         for (int i = 4; i < line.length; i++) {
            if (StringUtils.isNotBlank(line[i])) {
               jdbcTemplate.update(INSERT_MATCH_SQL, accountId, line[i]);
            }
         }
      }
   }

   private final String CATEGORY = "cat";
   private final String GROUP = "grp";
   private final String ACCOUNT = "acc";

   private String isAccountLine(String[] line) {
      String retVal = null;
      if (line != null && line.length > 3) {
         if (StringUtils.isNotBlank(line[0]) && StringUtils.isBlank(line[1]) && StringUtils.isBlank(line[2])) {
            LOG.debug("Found Category");
            retVal = CATEGORY;
         } else if (StringUtils.isBlank(line[0]) && StringUtils.isNotBlank(line[1]) && StringUtils.isBlank(line[2])) {
            LOG.debug("Found Group");
            retVal = GROUP;
         } else if (StringUtils.isBlank(line[0]) && StringUtils.isBlank(line[1]) && StringUtils.isNotBlank(line[2])) {
            LOG.debug("Found Account");
            retVal = ACCOUNT;
         }
      }
      return retVal;
   }
}
