use std::sync::Mutex;

use druid::{AppDelegate, Command, DelegateCtx, Env, Handled, Selector, Target};

use crate::{
    model::messages::{ActionData, Message},
    AppData,
};

pub const UDP_MSG_RECV: Selector<Message> = Selector::new("udp.rec");
pub const CHECK_FIELD: Selector<()> = Selector::new("field.check");

pub struct Delegate;
impl AppDelegate<AppData> for Delegate {
    fn command(
        &mut self,
        _ctx: &mut DelegateCtx,
        _target: Target,
        cmd: &Command,
        data: &mut AppData,
        _env: &Env,
    ) -> Handled {
        if cmd.is(UDP_MSG_RECV) {
            let message = cmd.get(UDP_MSG_RECV).unwrap();
            data.manager.rec_msg(message);

            return Handled::Yes;
        }
        if cmd.is(CHECK_FIELD) {
            data.manager.game.check();
        }
        Handled::No
    }
}
