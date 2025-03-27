pub mod game;
pub mod manager;
pub mod model;
pub mod udp;

use manager::Manager;

#[tokio::main]
async fn main() {
    let mut manager = Manager::new();
}
