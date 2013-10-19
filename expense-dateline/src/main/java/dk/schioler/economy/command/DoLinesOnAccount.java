package dk.schioler.economy.command;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.LinePersister;

@Component("doLinesOnAccount")
public class DoLinesOnAccount {

   static Logger LOG = Logger.getLogger(DoLinesOnAccount.class);

   @Autowired
   private LinePersister linePersister;

   @Autowired
   private AccountPersister accountPersister;

   private User user;

   private String accountFullPath;

   private PrintWriter writer;

   public void execute() {

      AccountTreeRoot accountTree = accountPersister.buildAccountTree(user.getId());
      List<Account> asList = null;
      if (StringUtils.isBlank(accountFullPath)) {
         asList = accountTree.getAsList();

      } else {
         Account topAccount = accountTree.findAccountOnFullPath(accountFullPath);
         asList = topAccount.getThisAndChildren();
      }

      for (Account account : asList) {
         List<Line> linesOn = linePersister.getLinesOn(user.getId(), account.getId());
         printAccountAndLines(account, linesOn);
      }

   }

   private void printAccountAndLines(Account a, List<Line> lines) {
      StringBuffer accountIndent = new StringBuffer();
      StringBuffer lineIndent = new StringBuffer();
      for (int i = 0; i < a.getLevel(); i++) {
         accountIndent.append("---");
         lineIndent.append("   ");
      }

      writer.println(accountIndent + a.getFullPath() + ", id=" + a.getId());
      for (Line line2 : lines) {
         writer.println(lineIndent + " " + line2);
      }
      writer.flush();

   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public String getAccountFullPath() {
      return accountFullPath;
   }


   public PrintWriter getWriter() {
      return writer;
   }

   public void setWriter(PrintWriter writer) {
      this.writer = writer;
   }

   public void setAccountFullPath(String accountFullPath) {
      this.accountFullPath = accountFullPath;
   }

}
