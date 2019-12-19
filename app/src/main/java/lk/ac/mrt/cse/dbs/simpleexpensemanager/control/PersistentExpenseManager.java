package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {

    Context context;
    public PersistentExpenseManager(Context context) {
        this.context=context;
        setup();
    }

    public void setup(){
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO inDBTransactionDAO = new InDBTransactionDAO(context);

//        TransactionDAO inDBTransactionDAO = new InMemoryTransactionDAO();
        setTransactionsDAO(inDBTransactionDAO);

        AccountDAO inDBAccountDAO = new InDBAccountDAO(context);
        setAccountsDAO(inDBAccountDAO);

        // dummy data
//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        Account dummyAcct1 = new Account("111111", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("222222", "Clone BC", "Obi-Wan Kenobi", 80000.0);

//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
