use std::sync::Mutex;

use druid::Target;
use tokio::net::UdpSocket;

use crate::{eve::UDP_MSG_RECV, model::messages::Message, AppData, PORT};

pub async fn run_server(event_sink: druid::ExtEventSink) -> std::io::Result<()> {
    let socket = UdpSocket::bind(format!("0.0.0.0:{}", PORT)).await?;
    println!("server is running");

    let mut buf: [u8; 1024] = [0; 1024];
    loop {
        let len = socket.recv(&mut buf).await?;
        let message: Message = serde_json::from_slice(&buf[..len])?;
        let _ = event_sink.submit_command(UDP_MSG_RECV, message, Target::Global);
    }
}
