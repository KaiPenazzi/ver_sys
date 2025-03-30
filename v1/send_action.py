import socket
import argparse
import json

def send_udp_message(x: int, y: int, usr: str = "tim"):
    """Sendet eine UDP-Nachricht mit x, y und Benutzername."""
    
    # Zieladresse und Port
    server_address = ('127.0.0.1', 1234)
    
    # Erstelle ein UDP-Socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    try:
        # Erstelle die Nachricht als JSON-String
        message = json.dumps({
            "type": "action",
            "data": {
                "x": x,
                "y": y,
                "usr": usr,
            }
        }).encode("utf-8")

        # Senden der Nachricht
        sock.sendto(message, server_address)
        print(f"Nachricht gesendet: {message.decode('utf-8')}")
    
    finally:
        sock.close()

if __name__ == "__main__":
    # ArgumentParser für CLI-Parameter
    parser = argparse.ArgumentParser(description="Sende eine UDP-Nachricht mit x, y und usr.")

    parser.add_argument("x", type=int, help="X-Koordinate")
    parser.add_argument("y", type=int, help="Y-Koordinate")
    parser.add_argument("usr", type=str, help="Benutzername", nargs="?", default="Tim")

    args = parser.parse_args()

    # Aufruf der Funktion mit CLI-Argumenten
    send_udp_message(args.x, args.y, args.usr)

