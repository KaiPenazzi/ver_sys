import socket
import argparse
import json

def main():
    parser = argparse.ArgumentParser(description="UDP Sender Script")
    parser.add_argument("port", type=int, help="Port number to send to (1-65535)")
    parser.add_argument("host", type=str, default="127.0.0.1", nargs="?", help="Target host (default: 127.0.0.1)")
    args = parser.parse_args()
    
    if not (1 <= args.port <= 65535):
        print("Error: Port number out of range (1-65535).")
        return
    
    message = {
        "type": "init",
        "data": {
            "field": [["None", "test", "None"], ["test", "None", "None"]],
            "k": 3,
            "scores": [{"usr":"test", "points": 2}, {"usr": "two","points": 13}]
        }
    }
    
    json_message = json.dumps(message)
    
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    
    try:
        sock.sendto(json_message.encode(), (args.host, args.port))
        print(f"Sent UDP message to {args.host}:{args.port}")
    except Exception as e:
        print(f"Error sending message: {e}")
    finally:
        sock.close()

if __name__ == "__main__":
    main()
