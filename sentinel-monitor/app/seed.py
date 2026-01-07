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

    print("🚀 Sentinel Seeder: Provisioning Multi-User Environment...")
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

        # 1. Create Schema with 'pin' and 'role' for Auth Support
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
                        balance DECIMAL(15,2),
                        currency VARCHAR(10)
                        );
                    CREATE TABLE IF NOT EXISTS transactions (
                                                                transaction_id SERIAL PRIMARY KEY,
                                                                from_account_id INTEGER,
                                                                to_account_id INTEGER,
                                                                amount DECIMAL(15,2),
                        status VARCHAR(50),
                        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );
                    """)

        print("🧹 Cleaning environment...")
        cur.execute("TRUNCATE TABLE transactions, accounts, users RESTART IDENTITY CASCADE;")

        # 2. Provision User A (Chuma)
        print("👤 Provisioning: Chuma Meyiswa (PIN: 1234)...")
        cur.execute("""
                    INSERT INTO users (full_name, email, pin, role)
                    VALUES (%s, %s, %s, %s) RETURNING user_id
                    """, ("Chuma Meyiswa", "nmeyiswa@gmail.com", "1234", "SENTINEL_PREMIUM"))
        chuma_id = cur.fetchone()[0]
        cur.execute("INSERT INTO accounts (user_id, balance, currency) VALUES (%s, 15000.50, 'ZAR')", (chuma_id,))

        # 3. Provision User B (Test Recipient)
        print("👤 Provisioning: Test Recipient (PIN: 5678)...")
        cur.execute("""
                    INSERT INTO users (full_name, email, pin, role)
                    VALUES (%s, %s, %s, %s) RETURNING user_id
                    """, ("Test Recipient", "test@sentinel.com", "5678", "SENTINEL_BASIC"))
        test_id = cur.fetchone()[0]
        cur.execute("INSERT INTO accounts (user_id, balance, currency) VALUES (%s, 2500.00, 'ZAR')", (test_id,))

        conn.commit()
        print("🎉 SUCCESS: Multi-user environment is LIVE.")
        print("👉 User 1: PIN 1234 | User 2: PIN 5678")

    except Exception as error:
        print(f"❌ Error: {error}")
        conn.rollback()
    finally:
        if conn:
            cur.close()
            conn.close()

if __name__ == "__main__":
    seed_data()