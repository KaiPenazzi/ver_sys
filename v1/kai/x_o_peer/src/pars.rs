use clap::Parser;

#[derive(Parser, Debug)]
#[command(version, about, long_about = None)]
pub struct Args {
    #[arg(short, long)]
    pub usr: String,

    #[arg(short = 'l', long = "url")]
    pub url: String,
}
