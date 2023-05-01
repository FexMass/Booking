export const isDateBlocked = (date, blocks) => {
  for (const block of blocks) {
    const blockStart = new Date(block.start);
    const blockEnd = new Date(block.end);

    if (date >= blockStart && date <= blockEnd) {
      return true;
    }
  }
  return false;
};

export const formatDate = (date) => {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}T00:00:00.000Z`;
};