use crate::model::messages::ActionData;

pub struct GameField {
    pub field: Vec<Vec<String>>,
    k: u32,
}

impl GameField {
    pub fn new(field: Vec<Vec<String>>, k: u32) -> Self {
        Self { field: field, k: k }
    }

    pub fn init(x: u32, y: u32, k: u32) -> Self {
        Self {
            field: vec![vec!["None".to_string(); y.try_into().unwrap()]; x.try_into().unwrap()],
            k: k,
        }
    }

    pub fn set(&mut self, action: &ActionData) {
        let x: usize = action.x.try_into().unwrap();
        let y: usize = action.y.try_into().unwrap();

        self.field[x][y] = action.usr.clone();
    }
}
