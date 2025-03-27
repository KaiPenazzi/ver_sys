// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]
#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub mod game;
pub mod manager;
pub mod model;
pub mod udp;

use std::sync::Arc;

use manager::Manager;
use tokio::sync::Mutex;

#[tokio::main]
async fn main() {
    let mut manager = Manager::new();
    let arc_manager = Arc::new(Mutex::new(manager));

    tauri::Builder::default()
        .setup(|app| {
            if cfg!(debug_assertions) {
                app.handle().plugin(
                    tauri_plugin_log::Builder::default()
                        .level(log::LevelFilter::Info)
                        .build(),
                )?;
            }
            Ok(())
        })
        .invoke_handler(tauri::generate_handler![start_game, do_nothing])
        .manage(arc_manager)
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}

#[tauri::command]
async fn start_game(
    arc_manager: tauri::State<'_, Arc<Mutex<Manager>>>,
) -> Result<(), tauri::Error> {
    let mut manager = arc_manager.lock().await;
    manager.start_game(1234, 3, 3, 3);
    println!("pressed start game");
    Ok(())
}

#[tauri::command]
async fn do_nothing() {
    println!("Tauri worked")
}
