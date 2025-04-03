use clap::Parser;

#[derive(Parser, Debug)]
#[command(version, about, long_about = None)]
pub struct Args {
    #[arg(short)]
    pub usr: String,

    #[arg(short)]
    pub port: String,

    #[arg()]
    pub urls: Vec<String>,
}
