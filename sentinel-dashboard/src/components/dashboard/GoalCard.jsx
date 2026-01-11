import React from 'react';
import { Target } from 'lucide-react';

const GoalCard = ({ goal }) => {
    // Calculate percentage (avoid division by zero)
    const percent = goal.targetAmount > 0
        ? Math.min(100, (goal.currentAmount / goal.targetAmount) * 100)
        : 0;

    return (
        <div className="card border-0 bg-light p-3 mb-3" style={{ borderRadius: '12px' }}>
            <div className="d-flex justify-content-between align-items-center mb-2">
                <div className="d-flex align-items-center gap-2">
                    <div className="bg-white p-2 rounded-circle shadow-sm">
                        <Target size={16} className="text-primary"/>
                    </div>
                    <span className="fw-bold">{goal.name}</span>
                </div>
                <small className="text-muted fw-bold">{percent.toFixed(0)}%</small>
            </div>

            <div className="progress" style={{ height: '8px', borderRadius: '10px', backgroundColor: '#e9ecef' }}>
                <div
                    className="progress-bar bg-primary"
                    role="progressbar"
                    style={{ width: `${percent}%`, borderRadius: '10px' }}
                ></div>
            </div>

            <div className="d-flex justify-content-between mt-2 small text-muted">
                <span>R {goal.currentAmount.toLocaleString()}</span>
                <span>Target: R {goal.targetAmount.toLocaleString()}</span>
            </div>
        </div>
    );
};

export default GoalCard;