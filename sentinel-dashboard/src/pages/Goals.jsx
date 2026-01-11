import React, { useEffect, useState } from 'react';
import { coreApi } from '../utils/api';
import { Target, Plus, Trophy, ArrowRight } from 'lucide-react';
import { toast } from 'react-toastify';
import CreateGoalModal from '../components/modals/CreateGoalModal';

const Goals = () => {
    const [goals, setGoals] = useState([]);
    const [accounts, setAccounts] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    // State for Contribution Logic
    const [contributeAmount, setContributeAmount] = useState('');
    const [selectedGoalId, setSelectedGoalId] = useState(null);
    const [selectedAccount, setSelectedAccount] = useState('');

    const fetchData = async () => {
        const gRes = await coreApi.get('/goals');
        const aRes = await coreApi.get('/accounts');
        setGoals(gRes.data);
        setAccounts(aRes.data);
        // Default select first account
        if (aRes.data.length > 0) setSelectedAccount(aRes.data[0].accountNumber);
    };

    useEffect(() => { fetchData(); }, []);

    const handleContribute = async (goalId) => {
        if (!contributeAmount || contributeAmount <= 0) return;
        try {
            await coreApi.post(`/goals/${goalId}/contribute`, {
                accountNumber: selectedAccount,
                amount: contributeAmount
            });
            toast.success("Funds Allocated to Goal! 🚀");
            setContributeAmount('');
            setSelectedGoalId(null);
            fetchData(); // Refresh data
        } catch (err) {
            toast.error("Contribution Failed (Check Balance)");
        }
    };

    return (
        <div className="container-fluid">
            {showCreateModal && <CreateGoalModal onClose={() => setShowCreateModal(false)} onGoalCreated={fetchData} />}

            <div className="d-flex justify-content-between align-items-center mb-4">
                <h3 className="fw-bold text-dark">My Goals</h3>
                <button onClick={() => setShowCreateModal(true)} className="btn btn-primary d-flex align-items-center gap-2 rounded-3 fw-bold">
                    <Plus size={18} /> New Goal
                </button>
            </div>

            <div className="row g-4">
                {goals.map(goal => {
                    const percent = Math.min(100, (goal.currentAmount / goal.targetAmount) * 100);
                    const isAchieved = percent >= 100;

                    return (
                        <div key={goal.goalId} className="col-md-6 col-lg-4">
                            <div className={`card border-0 shadow-sm p-4 h-100 rounded-4 ${isAchieved ? 'bg-success-subtle' : 'bg-white'}`}>
                                <div className="d-flex justify-content-between align-items-start mb-3">
                                    <div className="d-flex align-items-center gap-3">
                                        <div className={`p-3 rounded-circle shadow-sm ${isAchieved ? 'bg-success text-white' : 'bg-light text-primary'}`}>
                                            {isAchieved ? <Trophy size={24}/> : <Target size={24}/>}
                                        </div>
                                        <div>
                                            <h5 className="fw-bold mb-0">{goal.name}</h5>
                                            <small className="text-muted">{isAchieved ? 'COMPLETED' : 'IN PROGRESS'}</small>
                                        </div>
                                    </div>
                                    <h4 className="fw-bold">{percent.toFixed(0)}%</h4>
                                </div>

                                {/* Progress Bar */}
                                <div className="progress mb-3" style={{ height: '10px', borderRadius: '10px' }}>
                                    <div className={`progress-bar ${isAchieved ? 'bg-success' : 'bg-primary'}`} style={{ width: `${percent}%` }}></div>
                                </div>

                                <div className="d-flex justify-content-between small fw-bold text-muted mb-4">
                                    <span>Saved: R {goal.currentAmount.toLocaleString()}</span>
                                    <span>Target: R {goal.targetAmount.toLocaleString()}</span>
                                </div>

                                {/* Contribution Section */}
                                {!isAchieved && (
                                    <div className="mt-auto bg-light p-3 rounded-3">
                                        {selectedGoalId === goal.goalId ? (
                                            <div className="fade-in">
                                                <label className="small fw-bold text-muted mb-2">Move money from:</label>
                                                <select className="form-select form-select-sm mb-2"
                                                        value={selectedAccount} onChange={e => setSelectedAccount(e.target.value)}>
                                                    {accounts.map(acc => <option key={acc.accountId} value={acc.accountNumber}>{acc.accountName}</option>)}
                                                </select>
                                                <div className="input-group input-group-sm mb-2">
                                                    <span className="input-group-text">R</span>
                                                    <input type="number" className="form-control" placeholder="Amount"
                                                           value={contributeAmount} onChange={e => setContributeAmount(e.target.value)} autoFocus/>
                                                    <button onClick={() => handleContribute(goal.goalId)} className="btn btn-success"><ArrowRight size={14}/></button>
                                                </div>
                                                <button onClick={() => setSelectedGoalId(null)} className="btn btn-link btn-sm text-muted p-0">Cancel</button>
                                            </div>
                                        ) : (
                                            <button onClick={() => setSelectedGoalId(goal.goalId)} className="btn btn-outline-primary w-100 btn-sm fw-bold">
                                                Add Funds
                                            </button>
                                        )}
                                    </div>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default Goals;