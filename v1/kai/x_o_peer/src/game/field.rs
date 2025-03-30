use druid::{im::Vector, Data, Env, EventCtx, Lens, Target};

use crate::{eve::CHECK_FIELD, model::messages::ActionData};

#[derive(Clone, Data, Lens)]
pub struct GameField {
    pub cols: Vector<Row>,
    pub x: usize,
    pub y: usize,
    pub k: u32,
}

impl GameField {
    pub fn init(usr: &String, x: u32, y: u32, k: u32) -> Self {
        let mut field = Self {
            x: x as usize,
            y: y as usize,
            k: k,
            cols: Vector::new(),
        };

        for x_t in 0..x {
            field.cols.push_back(Row::new(x_t, y, usr));
        }

        field
    }

    pub fn set(&mut self, action: ActionData) {
        let x = action.x as usize;
        let y = action.y as usize;

        if let Some(row) = self.cols.get_mut(x) {
            if let Some(cell) = row.cells.get_mut(y) {
                cell.click(Some(&action.usr));
            }
        }
    }

    pub fn get(&self, x: u32, y: u32) -> Option<&Cell> {
        let x = x as usize;
        let y = y as usize;

        if let Some(row) = self.cols.get(x) {
            if let Some(cell) = row.cells.get(y) {
                return Some(cell);
            }
        }

        None
    }

    pub fn reset(&mut self, x: u32, y: u32) {
        let x = x as usize;
        let y = y as usize;

        if let Some(row) = self.cols.get_mut(x) {
            if let Some(cell) = row.cells.get_mut(y) {
                cell.reset()
            }
        }
    }

    pub fn check(&mut self) -> Option<String> {
        match self.check_col() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_row() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_diagonal_lr() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_diagonal_rl() {
            Some(name) => return Some(name),
            None => (),
        };

        None
    }

    fn check_col(&mut self) -> Option<String> {
        for col in self.cols.clone() {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<&Cell> = vec![];

            for cell in &col.cells {
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_row(&mut self) -> Option<String> {
        for y in 0..self.y {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];

            for x in 0..self.x {
                let cell = self
                    .get(x.try_into().unwrap(), y.try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_diagonal_lr(&mut self) -> Option<String> {
        for x in 0..self.x {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for y in (0..self.y).filter(|y| x + y < self.x) {
                let cell = self
                    .get((x + y).try_into().unwrap(), y.try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        for y in 0..self.y {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for x in (0..self.x).filter(|x| x + y < self.y) {
                let cell = self
                    .get(x.try_into().unwrap(), (x + y).try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_diagonal_rl(&mut self) -> Option<String> {
        for x in (0..self.x).rev() {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for y in (0..self.y).filter(|y| x - y >= 0) {
                let cell = self
                    .get(
                        (x - y).try_into().unwrap(),
                        (self.y - y).try_into().unwrap(),
                    )
                    .unwrap()
                    .clone();
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        for y in (0..self.y).rev() {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for x in (0..self.x).filter(|x| y - x <= 0) {
                let cell = self
                    .get((y - x).try_into().unwrap(), (x).try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "None" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }
}

#[derive(Clone, Data, Lens)]
pub struct Row {
    pub cells: Vector<Cell>,
}
impl Row {
    fn new(x: u32, y: u32, usr: &String) -> Self {
        let mut row = Self {
            cells: Vector::new(),
        };

        for y_t in 0..y {
            row.cells.push_back(Cell::new(usr, x, y_t));
        }

        row
    }
}

#[derive(Clone, Data, Lens)]
pub struct Cell {
    usr: String,
    pub text: String,
    pub x: u32,
    pub y: u32,
}
impl Cell {
    fn new(usr: &String, x: u32, y: u32) -> Self {
        Self {
            usr: usr.to_string(),
            text: "None".to_string(),
            x: x,
            y: y,
        }
    }

    pub fn click(&mut self, name: Option<&String>) {
        match name {
            Some(name) => self.text = name.to_string(),
            None => self.text = self.usr.clone(),
        }
    }

    pub fn on_click(_ctx: &mut EventCtx, data: &mut Cell, _env: &Env) {
        data.click(None);
        _ctx.submit_command(CHECK_FIELD);
    }

    pub fn reset(&mut self) {
        self.text = "None".to_string()
    }
}

#[cfg(test)]
mod test_field {
    use crate::model::messages::ActionData;

    use super::GameField;

    #[test]
    fn test_col() {
        let mut field = GameField::init(&"kai".to_string(), 3, 3, 2);
        field.set(ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 1,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_row() {
        let mut field = GameField::init(&"kai".to_string(), 3, 3, 2);
        field.set(ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 0,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_diag_lr() {
        let mut field = GameField::init(&"kai".to_string(), 3, 3, 2);
        field.set(ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 1,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_diag_rl() {
        let mut field = GameField::init(&"kai".to_string(), 4, 4, 3);
        field.set(ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 1,
        });

        field.set(ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 1,
        });

        field.set(ActionData {
            usr: "tim".to_string(),
            x: 0,
            y: 2,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }
}
