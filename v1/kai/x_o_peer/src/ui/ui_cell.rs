use druid::widget::{prelude::*, Axis};
use druid::{
    BoxConstraints, Color, Env, Event, EventCtx, LayoutCtx, LifeCycle, LifeCycleCtx, PaintCtx,
    Size, UpdateCtx, Widget,
};

use crate::game::field::Cell;

pub struct UiCell;

impl UiCell {
    pub fn new() -> Self {
        Self {}
    }
}

impl Widget<Cell> for UiCell {
    fn event(&mut self, _ctx: &mut EventCtx, _event: &Event, _data: &mut Cell, _env: &Env) {}

    fn lifecycle(&mut self, _ctx: &mut LifeCycleCtx, _event: &LifeCycle, _data: &Cell, _env: &Env) {
    }

    fn update(&mut self, ctx: &mut UpdateCtx, old_data: &Cell, data: &Cell, _env: &Env) {
        if data.text != old_data.text {
            ctx.request_layout();
        }
    }

    fn layout(
        &mut self,
        _ctx: &mut LayoutCtx,
        _bc: &BoxConstraints,
        _data: &Cell,
        _env: &Env,
    ) -> Size {
        Size::new(60., 60.0)
    }

    fn paint(&mut self, ctx: &mut PaintCtx, data: &Cell, _env: &Env) {
        let rect = ctx.size().to_rect();
        let color = Color::from_hex_str(&string_to_hex_color(&data.text)).unwrap();
        ctx.fill(rect, &color);
    }

    fn compute_max_intrinsic(
        &mut self,
        axis: Axis,
        _ctx: &mut LayoutCtx,
        _bc: &BoxConstraints,
        _data: &Cell,
        _env: &Env,
    ) -> f64 {
        match axis {
            Axis::Horizontal => 60.0,
            Axis::Vertical => 60.0,
        }
    }
}

use std::collections::hash_map::DefaultHasher;
use std::hash::{Hash, Hasher};

fn string_to_hex_color(input: &str) -> String {
    if input == "none" {
        return "#2d2d33".to_string();
    }

    let mut hasher = DefaultHasher::new();
    input.hash(&mut hasher);
    let hash = hasher.finish();

    let r = ((hash >> 16) & 0xFF) as u8;
    let g = ((hash >> 8) & 0xFF) as u8;
    let b = (hash & 0xFF) as u8;

    format!("#{:02x}{:02x}{:02x}", r, g, b)
}
