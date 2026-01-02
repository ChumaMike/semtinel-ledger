import psycopg2
import time
import sys

def seed_data():
    # Database Configuration
    db_config = {
        "dbname": "sentinel_ledger",
        "user": "user",
        "password": "password",
        "host": "127.0.0.1",
        "port": "5432"
    }

    conn = None
    retries = 5

    # 🧠 System Principle: Retry Logic (Wait-for-IT)
    print("🚀 Starting Database Seed...")
    while retries > 0:
        try:
            conn = psycopg2.connect(**db_config)
            print("✅ Connection Established!")
            break
        except psycopg2.OperationalError as e:
            retries -= 1
            print(f"⚠️ Database not ready. Retrying in 3 seconds... ({retries} attempts left)")
            time.sleep(3)

    if not conn:
        print("❌ CRITICAL: Could not connect to the database. Is the Docker container running?")
        sys.exit(1)

    try:
        cur = conn.cursor()

        # 1. Clear existing data to allow for clean re-runs (Idempotency)
        print("🧹 Cleaning old data...")
        cur.execute("TRUNCATE TABLE transactions, accounts, users RESTART IDENTITY CASCADE;")

        # 2. Insert Test User
        print("👤 Creating User...")
        cur.execute(
            "INSERT INTO users (full_name, email) VALUES (%s, %s) RETURNING user_id",
            ("Chuma Meyiswa", "nmeyiswa@gmail.com")
        )
        user_id = cur.fetchone()[0]

        # 3. Create Accounts (Savings and Checking)
        print("💰 Creating Accounts...")
        # Main Savings Account (ID: 1)
        cur.execute(
            "INSERT INTO accounts (user_id, balance, currency) VALUES (%s, %s, %s)",
            (user_id, 15000.50, "ZAR")
        )
        # Checking Account (ID: 2)
        cur.execute(
            "INSERT INTO accounts (user_id, balance, currency) VALUES (%s, %s, %s)",
            (user_id, 2500.00, "ZAR")
        )

        conn.commit()
        print(f"🌟 SUCCESS: Database seeded with User ID: {user_id}")

    except Exception as error:
        print(f"❌ Error during seeding: {error}")
        if conn:
            conn.rollback()
    finally:
        if conn:
            cur.close()
            conn.close()
            print("🔌 Database connection closed.")

if __name__ == "__main__":
    seed_data()