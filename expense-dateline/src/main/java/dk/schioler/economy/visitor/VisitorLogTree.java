package dk.schioler.economy.visitor;

import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;

public class VisitorLogTree implements Visitor {

   private static final Logger LOG = Logger.getLogger(VisitorLogTree.class);

   boolean includeLines;

   public VisitorLogTree() {
      this(false);
   }

   public VisitorLogTree(boolean includeLines) {
      super();
      this.includeLines = includeLines;
   }

   public boolean visit(Account element) {
      int level = element.getLevel();
      String prefix = "";
      for (int i = 0; i < level; i++) {
         prefix = prefix + "    ";
      }
      LOG.debug(prefix + element);
      if (includeLines) {
         List<Line> lines = element.getLines();
         for (Line line : lines) {
            LOG.debug("  " + prefix + line);
         }
      }
      return true;
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

}
