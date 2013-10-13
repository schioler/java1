package dk.schioler.economy.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorInsertAccount;
import dk.schioler.economy.visitor.VisitorLogTree;
import dk.schioler.economy.visitor.VisitorUpdateAccountUserId;

@Component("doAccountInsert")
@Scope("prototype")
public class DoAccountInsert extends BaseCommand {
//   private static final Logger LOG = Logger.getLogger(DoAccountInsert.class);

   @Autowired
   AccountPersister accountPersister;

   ;

   public void execute(String owner, AccountTreeRoot accountTreeRoot) {

      lookupUser(owner);


      VisitorUpdateAccountUserId visitor = new VisitorUpdateAccountUserId(user);
      accountTreeRoot.accept(visitor);

      Visitor logTree = new VisitorLogTree();
      accountTreeRoot.accept(logTree);

      accountPersister.deleteAllAccountsOnUser(user.getId());
      VisitorInsertAccount insertVisitor = new VisitorInsertAccount(accountPersister, user);
      insertVisitor.init();
      accountTreeRoot.accept(insertVisitor);

   }
}
