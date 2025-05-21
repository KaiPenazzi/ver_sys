import socket
import argparse

def get_local_ip():
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.connect(("8.8.8.8", 80))
            return s.getsockname()[0]
    except Exception:
        return "127.0.0.1"

def main():
    parser = argparse.ArgumentParser(description="UDP Listener Script")
    parser.add_argument("port", type=int, help="Port number to listen on (1-65535)")
    args = parser.parse_args()
    
    if not (1 <= args.port <= 65535):
        print("Error: Port number out of range (1-65535).")
        return
    
    local_ip = get_local_ip()
    print(f"Listening for UDP packets on {local_ip}:{args.port}...")
    
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind(("0.0.0.0", args.port))
    
    try:
        while True:
            data, addr = sock.recvfrom(2024)
            print(f"Received from {addr}: {data.decode(errors='ignore')}")
    except KeyboardInterrupt:
        print("\nStopping UDP listener.")
    finally:
        sock.close()

if __name__ == "__main__":
    main()

