
use druid::{AppDelegate, Command, DelegateCtx, Env, Handled, Selector, Target};

use crate::{
    game::field::Cell,
    model::com::RecvMsg,
    AppData,
};

pub const UDP_MSG_RECV: Selector<RecvMsg> = Selector::new("udp.rec");
pub const FIELD_CLICKED: Selector<Cell> = Selector::new("field.check");

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
        if cmd.is(FIELD_CLICKED) {
            let cell = cmd.get(FIELD_CLICKED).unwrap();
            data.manager.action(cell.x, cell.y)
        }
        Handled::No
    }
}
