use crate::model::messages::ActionData;

pub struct GameField {
    pub field: Vec<Vec<String>>,
    k: usize,
}

impl GameField {
    pub fn new(field: Vec<Vec<String>>, k: usize) -> Self {
        Self { field: field, k: k }
    }

    pub fn init(x: usize, y: usize, k: usize) -> Self {
        Self {
            field: vec![vec!["None".to_string(); y]; x],
            k: k,
        }
    }

    pub fn set(&mut self, action: &ActionData) {
        self.field[action.x][action.y] = action.usr.clone()
    }
}
