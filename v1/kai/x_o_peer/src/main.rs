pub mod game;
pub mod manager;
pub mod model;
pub mod udp;
mod ui;

use std::sync::{Arc, Mutex};

use druid::{AppLauncher, Data, Lens, WindowDesc};
use manager::Manager;
use ui::ui_builder;

#[derive(Clone, Data, Lens)]
struct AppData {
    manager: Arc<Mutex<Manager>>,
    input_port: String,
    input_x: String,
    input_y: String,
    input_k: String,
}

#[tokio::main]
async fn main() {
    let manager = Arc::new(Mutex::new(Manager::new("kai".to_string())));
    let app_data = AppData {
        manager: manager.clone(),
        input_port: "1234".to_string(),
        input_x: "3".to_string(),
        input_y: "3".to_string(),
        input_k: "3".to_string(),
    };

    let main_window = WindowDesc::new(ui_builder().await);

    AppLauncher::with_window(main_window)
        .log_to_console()
        .launch(app_data)
        .expect("launch failed");

    manager.lock().unwrap().stop();
}
