use druid::{im::Vector, Data, Env, EventCtx, Lens};

#[derive(Clone, Data, Lens)]
pub struct GameField {
    pub cols: Vector<Row>,
    pub k: u32,
}
impl GameField {
    pub fn init(x: u32, y: u32, k: u32) -> Self {
        let mut field = Self {
            k: k,
            cols: Vector::new(),
        };

        for _ in 0..x {
            field.cols.push_back(Row::new(y));
        }

        field
    }
}

#[derive(Clone, Data, Lens)]
pub struct Row {
    pub cells: Vector<Cell>,
}
impl Row {
    fn new(y: u32) -> Self {
        let mut row = Self {
            cells: Vector::new(),
        };

        for _ in 0..y {
            row.cells.push_back(Cell::new());
        }

        row
    }
}

#[derive(Clone, Data, Lens)]
pub struct Cell {
    pub text: String,
}
impl Cell {
    fn new() -> Self {
        Self {
            text: "None".to_string(),
        }
    }

    fn click(&mut self) {
        self.text = "click".to_string();
    }

    pub fn on_click(ctx: &mut EventCtx, data: &mut Cell, _env: &Env) {
        data.click()
    }
}
