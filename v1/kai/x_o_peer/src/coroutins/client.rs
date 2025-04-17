use std::sync::mpsc;

use tokio::net::UdpSocket;

use crate::model::{com::SendMsg, messages::Message};

pub async fn run_cleint(msg_queue: mpsc::Receiver<SendMsg>) -> tokio::io::Result<()> {
    let socket = UdpSocket::bind("0.0.0.0:0").await.unwrap();

    loop {
        let udp_msg = msg_queue.recv().unwrap();

        let json = parse(udp_msg.msg).unwrap();

        for peer in udp_msg.send_to {
            socket.send_to(json.as_bytes(), &peer.to_url()).await?;
        }
    }
}

fn parse(msg: Message) -> Option<String> {
    match msg {
        Message::Init(init) => return Some(serde_json::to_string(&init).unwrap()),
        Message::Action(action) => return Some(serde_json::to_string(&action).unwrap()),
        Message::Join(join) => return Some(serde_json::to_string(&join).unwrap()),
    }
}
