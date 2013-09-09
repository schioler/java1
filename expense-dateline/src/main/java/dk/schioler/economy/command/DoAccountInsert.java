package dk.schioler.economy.command;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.file.AccountFile;
import dk.schioler.economy.file.AccountFileReaderODS;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorInsertAccount;
import dk.schioler.economy.visitor.VisitorLogTree;
import dk.schioler.economy.visitor.VisitorUpdateAccountUserId;

@Component("doAccountInsert")
public class DoAccountInsert {
   private static final Logger LOG = Logger.getLogger(DoAccountInsert.class);

   @Autowired
   AccountPersister accountPersister;

   @Autowired
   UserPersister userPersister;

   public void doAccountInsert(String accountFile) {

      // lateron: offer multiple accountFile formats: excel, ods, csv,....
      AccountFile accountFileReader = new AccountFileReaderODS();
      AccountTreeRoot accountStructure = accountFileReader.buildAccountStructure(new File(accountFile));

      String username = accountFileReader.getUserName();
      User user = userPersister.getUser(username);
      if (user == null) {
         user = new User(null, username, null);
         user = userPersister.createUser(user);
      }

      VisitorUpdateAccountUserId visitor = new VisitorUpdateAccountUserId(user);
      accountStructure.accept(visitor);

      Visitor logTree = new VisitorLogTree();
      accountStructure.accept(logTree);

      accountPersister.deleteAllAccountsOnUser(user.getId());
      VisitorInsertAccount insertVisitor = new VisitorInsertAccount(accountPersister, user);
      insertVisitor.init();
      accountStructure.accept(insertVisitor);

   }
}
