import socket
import json

ip = "localhost"
port = 1225
from_address = f"{ip}:{port}"

message = {
    "type": "i",
    "body": {
        "from": from_address
    }
}

json_message = json.dumps(message)

target_ip = "localhost"
target_port = 1234

def send_udp_message(msg, target_ip, target_port):
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
        sock.sendto(msg.encode('utf-8'), (target_ip, target_port))
        print(f"UDP-Nachricht gesendet an {target_ip}:{target_port}")

if __name__ == "__main__":
    send_udp_message(json_message, target_ip, target_port)

