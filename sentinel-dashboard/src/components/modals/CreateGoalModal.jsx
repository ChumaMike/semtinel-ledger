import React, { useState } from 'react';
import { X, Target, Trophy } from 'lucide-react';
import { coreApi } from '../../utils/api'; // Note: check your path levels!
import { toast } from 'react-toastify';

const CreateGoalModal = ({ onClose, onGoalCreated }) => {
    const [name, setName] = useState('');
    const [amount, setAmount] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await coreApi.post('/goals', {
                name: name,
                targetAmount: amount
            });
            toast.success("New Goal Set!");
            onGoalCreated();
            onClose();
        } catch (err) {
            toast.error("Failed to create goal");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center"
             style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1070 }}>
            <div className="card border-0 shadow-lg p-4" style={{ width: '400px', borderRadius: '16px' }}>
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h5 className="fw-bold mb-0 d-flex align-items-center gap-2">
                        <Trophy className="text-warning"/> Set New Goal
                    </h5>
                    <button onClick={onClose} className="btn btn-light rounded-circle p-2">
                        <X size={20} />
                    </button>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label small fw-bold text-muted">GOAL NAME</label>
                        <input
                            type="text" className="form-control bg-light border-0 py-3"
                            placeholder="e.g. Dream Car"
                            value={name} onChange={e => setName(e.target.value)} required
                        />
                    </div>

                    <div className="mb-4">
                        <label className="form-label small fw-bold text-muted">TARGET AMOUNT (R)</label>
                        <input
                            type="number" className="form-control bg-light border-0 py-3 fw-bold"
                            placeholder="0.00"
                            value={amount} onChange={e => setAmount(e.target.value)} required
                        />
                    </div>

                    <button className="btn btn-primary w-100 py-3 fw-bold" disabled={loading}>
                        {loading ? 'Saving...' : 'Create Goal'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateGoalModal;