// JavaScript Document

function createRowsArray (table) {
  var rows = new Array();
  var r = 0;
  if (table.tHead == null && table.tFoot == null)
    for (var r1 = 0; r1 < table.rows.length; r1++, r++)
      rows[r] = table.rows[r1];
  else  
    for (var t = 0; t < table.tBodies.length; t++)
      for (var r1 = 0; r1 < table.tBodies[t].rows.length; r1++, r++)
        rows[r] = table.tBodies[t].rows[r1];
  return rows;
}
function insertSortedRows(table, rows) {
  if (document.all) var rowsCopy = new Array(rows.length)
  for (var r = 0; r < rows.length; r++) {
    if (document.all) rowsCopy[r] = rows[r].cloneNode(true);
    table.deleteRow(rows[r].rowIndex);
  }
  var tableSection = table.tBodies[table.tBodies.length - 1];
  for (var r = 0; r < rows.length; r++) {
    var row = document.all ? rowsCopy[r] : rows[r];
    tableSection.appendChild(row);
  }
}
function sortTable (table, sortFun) {
  var rows = createRowsArray(table);
  if (rows.length > 0) {
    rows.sort(sortFun);
    insertSortedRows(table, rows);
  }
}

function sortRowsAlpha (row1 , row2) {
  var column = sortRowsAlpha.col;
  var cell1 = row1.cells[column].firstChild.nodeValue;
  var cell2 = row2.cells[column].firstChild.nodeValue;
  return cell1 < cell2 ? - 1 : (cell1 == cell2 ? 0 : 1);
}
function sortRowsNumber (row1 , row2) {
  var column = sortRowsNumber.col;
  var cell1 = parseFloat(row1.cells[column].firstChild.nodeValue);
  var cell2 = parseFloat(row2.cells[column].firstChild.nodeValue);
  return cell1 < cell2 ? - 1 : (cell1 == cell2 ? 0 : 1);
}
function sortRowsLink (row1 , row2) {
  var column = sortRowsLink.col;
  var cell1 = findFirstLinkChild(row1.cells[column]).href;
  var cell2 = findFirstLinkChild(row2.cells[column]).href;
  return cell1 < cell2 ? - 1 : (cell1 == cell2 ? 0 : 1);
}
function findFirstLinkChild (el) {
  var child = el.firstChild;
  while (child.tagName != 'A')
    child = child.nextSibling;
  return child;
}
function testSortTable(table, col) {
  sortRowsAlpha.col = col;
  sortTable(table, sortRowsAlpha);
}
function testSortTableNumerical (table, col) {
  sortRowsNumber.col = col;
  sortTable(table, sortRowsNumber);
}
function testSortTableLinks (table, col) {
  sortRowsLink.col = col;
  sortTable(table, sortRowsLink);
}
function findTableParent (node) {
  while (node.tagName.toUpperCase() != 'TABLE')
    node = node.parentNode;
  return node;
}
