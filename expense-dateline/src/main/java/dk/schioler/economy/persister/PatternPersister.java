package dk.schioler.economy.persister;

import java.util.List;

import dk.schioler.economy.model.Pattern;

public interface PatternPersister {

   public Pattern createPattern(Long accountId, Pattern pattern);

   public Pattern getPattern(Long accountId, String pattern);

   public List<Pattern> getPatternsOnAccountId(Long accountId);

   public List<Pattern> getPatternsOnAccountIdList(List<Long> accountIdList);

   public List<Pattern> getPatternsOnUserId(Long accountId);


   public int deletePatterns(Long accountId);

   public int deleteAllPatternsOnUser(Long userId);
}
