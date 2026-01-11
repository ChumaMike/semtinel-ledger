import React, { useEffect, useState } from 'react';
import { coreApi } from '../utils/api';
import FinancialHealthChart from '../components/dashboard/FinancialHealthChart';
import { AlertCircle } from 'lucide-react';

const FinancialHealth = () => {
    const [transactions, setTransactions] = useState([]);
    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        coreApi.get('/accounts/history').then(res => setTransactions(res.data));
        coreApi.get('/accounts').then(res => setAccounts(res.data));
    }, []);

    const myAccountNumbers = accounts.map(a => a.accountNumber);

    // Filter only Expenses
    const expenses = transactions.filter(tx =>
        tx.transactionType === 'EXPENSE' ||
        (myAccountNumbers.includes(tx.senderAccountNumber) && tx.transactionType !== 'DEPOSIT')
    );

    const totalSpent = expenses.reduce((sum, tx) => sum + tx.amount, 0);

    return (
        <div className="container-fluid">
            <h3 className="fw-bold text-dark mb-4">Financial Health</h3>

            <div className="row g-4">
                {/* Left: The Chart */}
                <div className="col-md-5">
                    <FinancialHealthChart transactions={transactions} accounts={accounts} myAccountNumbers={myAccountNumbers} />

                    <div className="card border-0 shadow-sm mt-4 p-4 rounded-4 bg-danger bg-opacity-10">
                        <div className="d-flex align-items-center gap-3 text-danger">
                            <AlertCircle size={32} />
                            <div>
                                <h6 className="fw-bold mb-0">Total Spent</h6>
                                <h3 className="fw-bold mb-0">R {totalSpent.toLocaleString()}</h3>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Right: Detailed Expense List */}
                <div className="col-md-7">
                    <div className="card border-0 shadow-sm h-100 rounded-4 p-4">
                        <h5 className="fw-bold mb-3">Expense Breakdown</h5>
                        <div className="table-responsive">
                            <table className="table table-hover align-middle">
                                <thead className="table-light">
                                <tr>
                                    <th className="border-0 rounded-start">Description</th>
                                    <th className="border-0">Date</th>
                                    <th className="border-0 text-end rounded-end">Amount</th>
                                </tr>
                                </thead>
                                <tbody>
                                {expenses.map(tx => (
                                    <tr key={tx.transactionId}>
                                        <td className="fw-bold text-muted">{tx.description}</td>
                                        <td className="small text-muted">{tx.timestamp.split('T')[0]}</td>
                                        <td className="fw-bold text-danger text-end">- R {tx.amount.toLocaleString()}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default FinancialHealth;