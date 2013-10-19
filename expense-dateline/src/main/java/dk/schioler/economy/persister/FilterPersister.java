package dk.schioler.economy.persister;

import java.util.List;

import dk.schioler.economy.matcher.Filter;

public interface FilterPersister {

   public Filter createLineFilter(Long accountId, Filter pattern);

   public List<Filter> getLineFiltersOnAccountId(Long accountId);

   public List<Filter> getLineFiltersOnAccountIdList(List<Long> accountIdList);

   public List<Filter> getLineFiltersOnUserId(Long accountId);


   public int deleteLineFilters(Long accountId);

   public int deleteAllLineFiltersOnUser(Long userId);
}
