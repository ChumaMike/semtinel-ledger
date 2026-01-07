from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

app = FastAPI()

# 🌟 UPDATED: Now accepts Account Numbers (Strings) instead of IDs (Ints)
class TransactionRequest(BaseModel):
    amount: float
    from_account: str
    to_account: str

@app.get("/")
def read_root():
    return {"status": "Sentinel AI Online", "version": "2.0-RETAIL"}

@app.get("/api/monitor/alerts")
def get_alerts():
    # Placeholder: In the next step, we will make this real!
    return []

@app.post("/v1/scrutinize")
def analyze_transaction(tx: TransactionRequest):
    print(f"🕵️ SENTINEL WATCH: Analyzing transfer of R{tx.amount} from {tx.from_account} to {tx.to_account}")

    # 1. HARD RULE: Any transaction over R 10,000 is flagged immediately
    if tx.amount > 10000:
        return {
            "decision": "BLOCKED",
            "reason": "Amount exceeds R10,000 limit. Sentinel Audit required."
        }

    # 2. SUSPICIOUS PATTERN: Transfers to the specific "Test" account (for demo purposes)
    # We simulate a "Watchlist" check
    if tx.to_account == "9999999999":
        return {
            "decision": "BLOCKED",
            "reason": "Beneficiary account is on the Sentinel Watchlist."
        }

    # 3. VELOCITY CHECK (Simulated)
    # If the sender is the specific Savings account, we apply stricter rules
    if tx.from_account == "1001000001" and tx.amount > 5000:
        return {
            "decision": "BLOCKED",
            "reason": "Unusual high-value transfer for this savings profile."
        }

    # ✅ If clean
    return {"decision": "APPROVED", "reason": "Transaction within normal parameters."}