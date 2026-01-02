import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Shield, Landmark, Send, Activity } from 'lucide-react';

function App() {
    const [accounts, setAccounts] = useState([]);
    const [health, setHealth] = useState({ database: "...", sentinel: "..." });
    const [transfer, setTransfer] = useState({ fromId: '', toId: '', amount: '' });

    const fetchData = async () => {
        try {
            const accRes = await axios.get('http://localhost:8080/api/accounts');
            setAccounts(accRes.data);
            const healthRes = await axios.get('http://localhost:8080/api/accounts/health');
            setHealth(healthRes.data);
        } catch (e) { console.error("API connection error", e); }
    };

    useEffect(() => { fetchData(); }, []);

    const handleTransfer = async (e) => {
        e.preventDefault();
        try {
            const url = `http://localhost:8080/api/accounts/transfer?fromId=${transfer.fromId}&toId=${transfer.toId}&amount=${transfer.amount}`;
            const res = await axios.post(url);
            alert(res.data);
            fetchData(); // Refresh balances
        } catch (err) {
            alert(err.response?.data?.message || "Transfer Failed");
        }
    };

    return (
        <div className="container py-5">
            <div className="d-flex align-items-center mb-5">
                <Shield size={50} className="text-primary me-3" />
                <h1 className="display-4 fw-bold">Sentinel Ledger Console</h1>
            </div>

            <div className="row g-4">
                {/* System Health */}
                <div className="col-md-4">
                    <div className="card shadow-sm border-0 bg-light p-4">
                        <h4 className="mb-3"><Activity className="me-2"/>System Status</h4>
                        <div className="d-flex justify-content-between mb-2">
                            <span>Vault (DB):</span>
                            <span className={`badge ${health.database === 'UP' ? 'bg-success' : 'bg-danger'}`}>{health.database}</span>
                        </div>
                        <div className="d-flex justify-content-between">
                            <span>Sentinel (AI):</span>
                            <span className={`badge ${health.sentinel === 'UP' ? 'bg-success' : 'bg-danger'}`}>{health.sentinel}</span>
                        </div>
                    </div>

                    <div className="card shadow-sm border-0 mt-4 p-4">
                        <h4 className="mb-3"><Send className="me-2"/>Quick Transfer</h4>
                        <form onSubmit={handleTransfer}>
                            <input type="number" placeholder="From ID" className="form-control mb-2" onChange={e => setTransfer({...transfer, fromId: e.target.value})} required />
                            <input type="number" placeholder="To ID" className="form-control mb-2" onChange={e => setTransfer({...transfer, toId: e.target.value})} required />
                            <input type="number" placeholder="Amount (R)" className="form-control mb-3" onChange={e => setTransfer({...transfer, amount: e.target.value})} required />
                            <button className="btn btn-primary w-100 fw-bold">Execute Transaction</button>
                        </form>
                    </div>
                </div>

                {/* Account Table */}
                <div className="col-md-8">
                    <div className="card shadow-sm border-0 p-4">
                        <h4 className="mb-3"><Landmark className="me-2"/>Live Ledger</h4>
                        <table className="table align-middle">
                            <thead className="table-light">
                            <tr><th>ID</th><th>Balance</th><th>Currency</th></tr>
                            </thead>
                            <tbody>
                            {accounts.map(acc => (
                                <tr key={acc.accountId}>
                                    <td className="fw-bold">#{acc.accountId}</td>
                                    <td className="text-success fw-bold">R {acc.balance.toFixed(2)}</td>
                                    <td><span className="badge bg-secondary">{acc.currency}</span></td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default App;