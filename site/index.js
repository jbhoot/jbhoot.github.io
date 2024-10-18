const addListRoleToTimelines = () => {
    // To fix Safari's decision to remove list role from
    // those lists which have `list-style-type: none` set in CSS:
    // https://www.scottohara.me/blog/2019/01/12/lists-and-safari.html
    const nodes = document.querySelectorAll('.list-style-type\\:none');
    for (const node of nodes) {
        node.setAttribute('role', 'list');
    }
};

const addLinkButtonToHeadings = () => {
    const headings = document.querySelectorAll(':is(h1, h2, h3, h4, h5, h6)');
    for (const heading of headings) {
        if (heading.id) {
            const btn = document.createElement('button');
            btn.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-link "><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"></path><path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path></svg>
            `;
            
            btn.type = 'button';
            btn.onclick = async e => {
                const linkToCopy = window.location.href + "#" + heading.id;
                await navigator.clipboard.writeText(linkToCopy);
            };
            heading.parentNode.insertBefore(btn, heading);
        }
    }
};

document.addEventListener('DOMContentLoaded', function () {
    addListRoleToTimelines();
    addLinkButtonToHeadings();
});