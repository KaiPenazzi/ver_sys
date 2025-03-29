use tokio::net::UdpSocket;

use crate::AppData;

pub async fn run_server(event_sink: druid::ExtEventSink) -> std::io::Result<()> {
    let socket = UdpSocket::bind(format!("0.0.0.0:{}", "1234")).await?;

    let mut buf: [u8; 1024] = [0; 1024];
    loop {
        let len = socket.recv(&mut buf).await?;
    }
}
