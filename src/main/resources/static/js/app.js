// DevOps Task Hub - Frontend Logic
document.addEventListener('DOMContentLoaded', () => {
    // State
    let tasks = [];
    let currentTab = 'tasksTab';

    // Elements
    const tasksList = document.getElementById('tasksList');
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const priorityFilter = document.getElementById('priorityFilter');
    const btnNewTask = document.getElementById('btnNewTask');
    const taskModal = document.getElementById('taskModal');
    const btnCloseModal = document.getElementById('btnCloseModal');
    const btnCancelModal = document.getElementById('btnCancelModal');
    const taskForm = document.getElementById('taskForm');
    const modalTitle = document.getElementById('modalTitle');
    const themeToggle = document.getElementById('themeToggle');
    const navTabs = document.querySelectorAll('.nav-tab');

    // KPI Elements
    const kpiTotal = document.getElementById('kpiTotal');
    const kpiInProgress = document.getElementById('kpiInProgress');
    const kpiDone = document.getElementById('kpiDone');
    const kpiCritical = document.getElementById('kpiCritical');

    // Health / Telemetry Elements
    const healthPill = document.getElementById('healthPill');
    const healthStatusText = document.getElementById('healthStatusText');
    const btnRefreshMetrics = document.getElementById('btnRefreshMetrics');

    // API Console Elements
    const apiEndpointButtons = document.querySelectorAll('.api-endpoint-btn');
    const currentMethod = document.getElementById('currentMethod');
    const apiEndpointInput = document.getElementById('apiEndpointInput');
    const requestBodyContainer = document.getElementById('requestBodyContainer');
    const apiRequestBody = document.getElementById('apiRequestBody');
    const btnSendApiRequest = document.getElementById('btnSendApiRequest');
    const apiStatusBadge = document.getElementById('apiStatusBadge');
    const apiLatencyText = document.getElementById('apiLatencyText');
    const apiResponseViewer = document.getElementById('apiResponseViewer');

    // Theme setup
    const savedTheme = localStorage.getItem('theme') || 'dark';
    if (savedTheme === 'light') {
        document.body.classList.remove('dark-mode');
        document.body.classList.add('light-mode');
        themeToggle.textContent = '☀️';
    }

    themeToggle.addEventListener('click', () => {
        const isLight = document.body.classList.toggle('light-mode');
        document.body.classList.toggle('dark-mode', !isLight);
        themeToggle.textContent = isLight ? '☀️' : '🌙';
        localStorage.setItem('theme', isLight ? 'light' : 'dark');
    });

    // Navigation Tab Switching
    navTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            navTabs.forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            tab.classList.add('active');
            const target = tab.getAttribute('data-tab');
            document.getElementById(target).classList.add('active');
            currentTab = target;

            if (target === 'telemetryTab') {
                fetchTelemetry();
            }
        });
    });

    // Fetch Health
    async function checkHealth() {
        try {
            const res = await fetch('/api/health');
            if (res.ok) {
                const data = await res.json();
                healthStatusText.textContent = `Online (${data.application})`;
                const dot = healthPill.querySelector('.status-dot');
                dot.className = 'status-dot healthy';
            } else {
                healthStatusText.textContent = 'Degraded';
                healthPill.querySelector('.status-dot').className = 'status-dot unhealthy';
            }
        } catch (e) {
            healthStatusText.textContent = 'Offline';
            healthPill.querySelector('.status-dot').className = 'status-dot unhealthy';
        }
    }

    // Fetch Tasks
    async function fetchTasks() {
        try {
            const res = await fetch('/api/tasks');
            if (res.ok) {
                tasks = await res.json();
                renderTasks();
                updateKPIs();
            }
        } catch (err) {
            tasksList.innerHTML = `<div class="empty-state">Error loading tasks: ${err.message}</div>`;
        }
    }

    // Update KPI counters
    function updateKPIs() {
        const total = tasks.length;
        const inProgress = tasks.filter(t => t.status === 'IN_PROGRESS').length;
        const done = tasks.filter(t => t.status === 'DONE').length;
        const critical = tasks.filter(t => t.priority === 'CRITICAL' || t.priority === 'HIGH').length;

        kpiTotal.textContent = total;
        kpiInProgress.textContent = inProgress;
        kpiDone.textContent = done;
        kpiCritical.textContent = critical;
    }

    // Render Tasks Grid
    function renderTasks() {
        const query = searchInput.value.toLowerCase().trim();
        const statusVal = statusFilter.value;
        const priorityVal = priorityFilter.value;

        const filtered = tasks.filter(t => {
            const matchesStatus = !statusVal || t.status === statusVal;
            const matchesPriority = !priorityVal || t.priority === priorityVal;
            const matchesSearch = !query || 
                (t.title && t.title.toLowerCase().includes(query)) ||
                (t.description && t.description.toLowerCase().includes(query)) ||
                (t.assignee && t.assignee.toLowerCase().includes(query)) ||
                (t.tags && t.tags.some(tag => tag.toLowerCase().includes(query)));
            return matchesStatus && matchesPriority && matchesSearch;
        });

        if (filtered.length === 0) {
            tasksList.innerHTML = '<div class="empty-state">No matching tasks found. Create one or clear filters!</div>';
            return;
        }

        tasksList.innerHTML = filtered.map(t => {
            const statusClass = `badge-${t.status.toLowerCase().replace('_', '')}`;
            const priorityClass = `badge-${t.priority.toLowerCase()}`;
            const tagsHtml = (t.tags || []).map(tag => `<span class="tag-pill">#${escapeHtml(tag)}</span>`).join('');

            return `
                <div class="task-card">
                    <div class="task-header">
                        <h4 class="task-title">${escapeHtml(t.title)}</h4>
                        <div class="task-badges">
                            <span class="badge ${priorityClass}">${t.priority}</span>
                            <span class="badge ${statusClass}">${formatStatus(t.status)}</span>
                        </div>
                    </div>
                    <p class="task-desc">${escapeHtml(t.description || 'No description provided.')}</p>
                    <div class="task-meta">
                        <div class="task-tags">${tagsHtml}</div>
                        <div class="task-assignee">👤 ${escapeHtml(t.assignee || 'Unassigned')}</div>
                    </div>
                    <div class="task-meta" style="border-top: none; padding-top: 8px;">
                        <span style="font-size: 11px; color: var(--text-secondary);">ID: #${t.id}</span>
                        <div class="task-actions">
                            <button class="btn-small" onclick="cycleStatus(${t.id})">↻ Next Status</button>
                            <button class="btn-small" onclick="editTask(${t.id})">✏️ Edit</button>
                            <button class="btn-small danger" onclick="deleteTask(${t.id})">🗑️</button>
                        </div>
                    </div>
                </div>
            `;
        }).join('');
    }

    function formatStatus(status) {
        if (status === 'IN_PROGRESS') return 'In Progress';
        if (status === 'DONE') return 'Done';
        return 'To Do';
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>'"]/g, 
            tag => ({
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                "'": '&#39;',
                '"': '&quot;'
            }[tag] || tag)
        );
    }

    // Modal Handlers
    function openModal(task = null) {
        taskForm.reset();
        if (task) {
            modalTitle.textContent = 'Edit Task';
            document.getElementById('taskId').value = task.id;
            document.getElementById('taskTitleInput').value = task.title;
            document.getElementById('taskDescriptionInput').value = task.description || '';
            document.getElementById('taskPriorityInput').value = task.priority;
            document.getElementById('taskStatusInput').value = task.status;
            document.getElementById('taskAssigneeInput').value = task.assignee || '';
            document.getElementById('taskTagsInput').value = (task.tags || []).join(', ');
        } else {
            modalTitle.textContent = 'Create New Task';
            document.getElementById('taskId').value = '';
        }
        taskModal.classList.add('open');
    }

    function closeModal() {
        taskModal.classList.remove('open');
    }

    btnNewTask.addEventListener('click', () => openModal());
    btnCloseModal.addEventListener('click', closeModal);
    btnCancelModal.addEventListener('click', closeModal);
    taskModal.addEventListener('click', (e) => {
        if (e.target === taskModal) closeModal();
    });

    // Form Submit
    taskForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('taskId').value;
        const title = document.getElementById('taskTitleInput').value.trim();
        const description = document.getElementById('taskDescriptionInput').value.trim();
        const priority = document.getElementById('taskPriorityInput').value;
        const status = document.getElementById('taskStatusInput').value;
        const assignee = document.getElementById('taskAssigneeInput').value.trim();
        const tags = document.getElementById('taskTagsInput').value
            .split(',')
            .map(s => s.trim())
            .filter(Boolean);

        const payload = { title, description, priority, status, assignee, tags };

        try {
            let res;
            if (id) {
                res = await fetch(`/api/tasks/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            } else {
                res = await fetch('/api/tasks', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            }

            if (res.ok) {
                closeModal();
                await fetchTasks();
            } else {
                const err = await res.json();
                alert('Error saving task: ' + (err.error || res.statusText));
            }
        } catch (err) {
            alert('Failed to connect to server: ' + err.message);
        }
    });

    // Status Cycle
    window.cycleStatus = async function(id) {
        const task = tasks.find(t => t.id === id);
        if (!task) return;

        let nextStatus = 'TODO';
        if (task.status === 'TODO') nextStatus = 'IN_PROGRESS';
        else if (task.status === 'IN_PROGRESS') nextStatus = 'DONE';
        else nextStatus = 'TODO';

        try {
            const res = await fetch(`/api/tasks/${id}/status`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ status: nextStatus })
            });
            if (res.ok) {
                await fetchTasks();
            }
        } catch (e) {
            console.error('Failed to update status', e);
        }
    };

    // Edit Task
    window.editTask = function(id) {
        const task = tasks.find(t => t.id === id);
        if (task) openModal(task);
    };

    // Delete Task
    window.deleteTask = async function(id) {
        if (!confirm(`Are you sure you want to delete task #${id}?`)) return;
        try {
            const res = await fetch(`/api/tasks/${id}`, { method: 'DELETE' });
            if (res.ok) {
                await fetchTasks();
            }
        } catch (err) {
            alert('Failed to delete task: ' + err.message);
        }
    };

    // Filters
    searchInput.addEventListener('input', renderTasks);
    statusFilter.addEventListener('change', renderTasks);
    priorityFilter.addEventListener('change', renderTasks);

    // Telemetry Tab
    async function fetchTelemetry() {
        try {
            const res = await fetch('/api/system/metrics');
            if (res.ok) {
                const m = await res.json();
                document.getElementById('metricAppName').textContent = m.applicationName;
                document.getElementById('metricUptime').textContent = m.formattedUptime;
                document.getElementById('metricStatus').textContent = m.status;
                document.getElementById('metricJavaVersion').textContent = `${m.javaVersion} (${m.javaVendor})`;
                document.getElementById('metricCores').textContent = m.availableProcessors;
                document.getElementById('metricThreads').textContent = m.activeThreadCount;
                document.getElementById('metricOS').textContent = `${m.osName} (${m.osArch})`;

                document.getElementById('memUsedText').textContent = `Used: ${m.usedMemoryMb} MB`;
                document.getElementById('memTotalText').textContent = `Total: ${m.totalMemoryMb} MB`;
                document.getElementById('memPercentText').textContent = `${m.memoryUsagePercent}% allocated`;
                document.getElementById('memoryProgressBar').style.width = `${Math.min(100, m.memoryUsagePercent)}%`;
            }
        } catch (err) {
            console.error('Failed to fetch telemetry', err);
        }
    }

    btnRefreshMetrics.addEventListener('click', fetchTelemetry);

    // API Console
    apiEndpointButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            apiEndpointButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const method = btn.getAttribute('data-method');
            const path = btn.getAttribute('data-path');
            const body = btn.getAttribute('data-body');

            currentMethod.textContent = method;
            apiEndpointInput.value = path;

            if (method === 'POST' || method === 'PUT') {
                requestBodyContainer.style.display = 'flex';
                try {
                    apiRequestBody.value = JSON.stringify(JSON.parse(body || '{}'), null, 2);
                } catch {
                    apiRequestBody.value = body || '';
                }
            } else {
                requestBodyContainer.style.display = 'none';
            }
        });
    });

    btnSendApiRequest.addEventListener('click', async () => {
        const method = currentMethod.textContent.trim();
        const url = apiEndpointInput.value.trim();
        const bodyStr = apiRequestBody.value.trim();

        const startTime = performance.now();
        try {
            const options = { method, headers: {} };
            if ((method === 'POST' || method === 'PUT' || method === 'PATCH') && bodyStr) {
                options.headers['Content-Type'] = 'application/json';
                options.body = bodyStr;
            }

            const res = await fetch(url, options);
            const latency = Math.round(performance.now() - startTime);
            apiLatencyText.textContent = `${latency} ms`;
            apiStatusBadge.textContent = `${res.status} ${res.statusText}`;

            if (res.status === 204) {
                apiResponseViewer.textContent = '204 No Content (Success)';
            } else {
                const text = await res.text();
                try {
                    const json = JSON.parse(text);
                    apiResponseViewer.textContent = JSON.stringify(json, null, 2);
                } catch {
                    apiResponseViewer.textContent = text;
                }
            }
            // Refresh tasks if modifying call was made
            if (method !== 'GET') {
                fetchTasks();
            }
        } catch (err) {
            const latency = Math.round(performance.now() - startTime);
            apiLatencyText.textContent = `${latency} ms`;
            apiStatusBadge.textContent = 'Error';
            apiResponseViewer.textContent = err.message;
        }
    });

    // Initial Load
    checkHealth();
    fetchTasks();
    setInterval(checkHealth, 15000);
});
