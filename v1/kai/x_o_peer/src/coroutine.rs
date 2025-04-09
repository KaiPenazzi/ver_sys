use std::sync::mpsc;

use druid::Target;
use serde::{Deserialize, Serialize};
use serde_json::Value;
use tokio::net::UdpSocket;

use crate::{
    eve::UDP_MSG_RECV,
    game::scores::Score,
    model::{
        com::{RecvMsg, SendMsg},
        messages::Message,
    },
};

#[derive(Debug, Deserialize, Serialize)]
struct ActionJson {
    pub r#type: String,
    pub usr: String,
    pub x: u32,
    pub y: u32,
}

#[derive(Debug, Deserialize, Serialize)]
struct InitJson {
    pub r#type: String,
    pub field: Vec<Vec<String>>,
    pub score: Vec<Score>,
    pub k: u32,
}

#[derive(Debug, Deserialize, Serialize)]
struct Join {
    pub r#type: String,
}

#[derive(Debug)]
enum ParsedMessage {
    Action(ActionJson),
    Init(InitJson),
    Join(Join),
}

fn parse_message(buf: &[u8]) -> Option<ParsedMessage> {
    // Erstmal generisch in ein Value parsen
    let val: Value = serde_json::from_slice(buf).ok()?;

    // "type" Feld extrahieren
    let msg_type = val.get("type")?.as_str()?;

    // Matchen und je nach Typ in die richtige Struct parsen
    match msg_type {
        "action" => serde_json::from_value::<ActionJson>(val)
            .ok()
            .map(ParsedMessage::Action),
        "init" => serde_json::from_value::<InitJson>(val)
            .ok()
            .map(ParsedMessage::Init),
        "join" => serde_json::from_value::<Join>(val)
            .ok()
            .map(ParsedMessage::Join),
        _ => None,
    }
}

pub async fn run_server(event_sink: druid::ExtEventSink, port: String) -> std::io::Result<()> {
    let socket = UdpSocket::bind(format!("0.0.0.0:{}", port)).await?;
    println!("server is running");

    let mut buf: [u8; 1024] = [0; 1024];
    loop {
        let (len, addr) = socket.recv_from(&mut buf).await?;

        let message: Message = serde_json::from_slice(&buf[..len])?;

        let _ = event_sink.submit_command(
            UDP_MSG_RECV,
            RecvMsg {
                msg: message,
                from: addr.ip(),
            },
            Target::Global,
        );
    }
}

pub async fn run_cleint(msg_queue: mpsc::Receiver<SendMsg>) -> tokio::io::Result<()> {
    let socket = UdpSocket::bind("0.0.0.0:0").await.unwrap();

    loop {
        let udp_msg = msg_queue.recv().unwrap();
        let json = serde_json::to_string(&udp_msg.msg).unwrap();

        for peer in udp_msg.send_to {
            socket.send_to(json.as_bytes(), &peer.to_url()).await?;
        }
    }
}
