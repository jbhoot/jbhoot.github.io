function addListRoleToTimelines() {
    // To fix Safari's decision to remove list role from
    // those lists which have `list-style-type: none` set in CSS:
    // https://www.scottohara.me/blog/2019/01/12/lists-and-safari.html
    const nodes = document.querySelectorAll('.list-style-type\\:none');
    for (const node of nodes) {
        node.setAttribute('role', 'list');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    addListRoleToTimelines();
});