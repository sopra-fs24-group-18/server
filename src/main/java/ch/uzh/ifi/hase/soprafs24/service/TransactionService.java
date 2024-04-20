package ch.uzh.ifi.hase.soprafs24.service;

import java.util.concurrent.Callable;

public interface TransactionService {

    void begin();

    void commit();

    void rollback();

    /**
     * 在事务中执行回调函数
     * 谨慎使用
     * @param callable 回调函数
     */
    <V> V execute(Callable<V> callable);

}
