use std::sync::mpsc;

use druid::Target;
use tokio::net::UdpSocket;

use crate::{
    eve::UDP_MSG_RECV,
    model::{
        com::{RecvMsg, SendMsg},
        messages::Message,
    },
    AppData, PORT,
};

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
