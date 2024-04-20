package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.Callable;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private ThreadLocal<TransactionStatus> threadLocal = new ThreadLocal<>();

    @Override
    public void begin() {
        // TransactionDefinition.PROPAGATION_REQUIRES_NEW 新开一个事务
        TransactionDefinition transactionDefinition =
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        threadLocal.set(transactionManager.getTransaction(transactionDefinition));
    }

    @Override
    public void commit() {
        transactionManager.commit(threadLocal.get());
        threadLocal.remove();
    }

    @Override
    public void rollback() {
        transactionManager.rollback(threadLocal.get());
        threadLocal.remove();
    }

    @Override
    public <V> V execute(Callable<V> callable) {
        try {
            begin();
            V result = callable.call();
            commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw new RuntimeException(e);
        }
    }
}
