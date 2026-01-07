import psycopg2
import time
import sys

def seed_data():
    db_config = {
        "dbname": "sentinel_db",
        "user": "user",
        "password": "password",
        "host": "db",
        "port": "5432"
    }

    conn = None
    retries = 10

    print("🚀 Sentinel Seeder: Initializing Retail Banking Environment...")
    while retries > 0:
        try:
            conn = psycopg2.connect(**db_config)
            print("✅ Connection Established!")
            break
        except Exception as e:
            retries -= 1
            print(f"⚠️ Database not ready. Retrying in 5s... ({retries} left)")
            time.sleep(5)

    if not conn:
        print("❌ CRITICAL: Database unreachable.")
        sys.exit(1)

    try:
        cur = conn.cursor()

        # 1. Create Schema (Retail Banking Structure)
        cur.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                                                         user_id SERIAL PRIMARY KEY,
                                                         full_name VARCHAR(255),
                        email VARCHAR(255),
                        pin VARCHAR(10),
                        role VARCHAR(50)
                        );

                    CREATE TABLE IF NOT EXISTS accounts (
                                                            account_id SERIAL PRIMARY KEY,
                                                            user_id INTEGER REFERENCES users(user_id),
                        account_number VARCHAR(20) UNIQUE NOT NULL,
                        account_type VARCHAR(50),
                        balance DECIMAL(15,2),
                        currency VARCHAR(10)
                        );

                    CREATE TABLE IF NOT EXISTS cards (
                                                         card_id SERIAL PRIMARY KEY,
                                                         card_number VARCHAR(16) UNIQUE NOT NULL,
                        card_name VARCHAR(50),
                        expiry_date VARCHAR(5),
                        cvv VARCHAR(3),
                        account_id INTEGER REFERENCES accounts(account_id),
                        user_id INTEGER REFERENCES users(user_id)
                        );

                    CREATE TABLE IF NOT EXISTS transactions (
                                                                transaction_id SERIAL PRIMARY KEY,
                                                                from_account_id INTEGER,
                                                                to_account_id INTEGER,
                                                                sender_account_number VARCHAR(20),
                        receiver_account_number VARCHAR(20),
                        description VARCHAR(255),
                        amount DECIMAL(15,2),
                        status VARCHAR(50),
                        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );
                    """)

        print("🧹 Purging old data...")
        cur.execute("TRUNCATE TABLE transactions, cards, accounts, users RESTART IDENTITY CASCADE;")

        # 2. Provision Chuma (2 Accounts)
        print("👤 Provisioning: Chuma Meyiswa (PIN: 1234)...")
        cur.execute("INSERT INTO users (full_name, email, pin, role) VALUES (%s, %s, %s, %s) RETURNING user_id", ("Chuma Meyiswa", "nmeyiswa@gmail.com", "1234", "SENTINEL_PREMIUM"))
        chuma_id = cur.fetchone()[0]

        # Savings Account
        cur.execute("INSERT INTO accounts (user_id, account_number, account_type, balance, currency) VALUES (%s, %s, %s, %s, %s) RETURNING account_id",
                    (chuma_id, "1001000001", "SAVINGS", 15000.50, "ZAR"))
        chuma_sav_id = cur.fetchone()[0]

        # Cheque Account
        cur.execute("INSERT INTO accounts (user_id, account_number, account_type, balance, currency) VALUES (%s, %s, %s, %s, %s)",
                    (chuma_id, "1001000002", "CHEQUE", 5000.00, "ZAR"))

        # Card for Savings
        cur.execute("INSERT INTO cards (card_number, card_name, expiry_date, cvv, account_id, user_id) VALUES (%s, %s, %s, %s, %s, %s)",
                    ("4532000011112222", "Gold Debit", "12/28", "123", chuma_sav_id, chuma_id))

        # 3. Provision Test User (1 Account)
        print("👤 Provisioning: Test Recipient (PIN: 5678)...")
        cur.execute("INSERT INTO users (full_name, email, pin, role) VALUES (%s, %s, %s, %s) RETURNING user_id", ("Test Recipient", "test@sentinel.com", "5678", "SENTINEL_BASIC"))
        test_id = cur.fetchone()[0]

        cur.execute("INSERT INTO accounts (user_id, account_number, account_type, balance, currency) VALUES (%s, %s, %s, %s, %s)",
                    (test_id, "2002000001", "SAVINGS", 2500.00, "ZAR"))

        conn.commit()
        print("🎉 SUCCESS: Retail Banking Environment is LIVE.")
        print("👉 Chuma: 1001000001 (Savings) | 1001000002 (Cheque)")
        print("👉 Test:  2002000001 (Savings)")

    except Exception as error:
        print(f"❌ Error: {error}")
        conn.rollback()
    finally:
        if conn:
            cur.close()
            conn.close()

if __name__ == "__main__":
    seed_data()