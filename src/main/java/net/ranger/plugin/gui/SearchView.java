package net.ranger.plugin.gui;

import net.ranger.plugin.actions.ChangeSearchDepthAction;
import net.ranger.plugin.actions.CollapseAllAction;
import net.ranger.plugin.actions.DefaultGroupingAction;
import net.ranger.plugin.actions.ExpandAllAction;
import net.ranger.plugin.actions.GroupByClassAction;
import net.ranger.plugin.actions.GroupByProjectAction;
import net.ranger.plugin.actions.OpenInEditorAction;
import net.ranger.plugin.actions.StopSearchAction;
import net.ranger.plugin.integration.RangerComponentFactoryImpl;
import net.ranger.search.SearchResult;
import net.ranger.view.SearchResultView;
import net.ranger.view.SearchResultViewModel;
import net.ranger.view.SearchViewException;
import net.ranger.view.SearchViewNode;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This is the view where search results are displayed.
 * 
 * @author Emerson Loureiro
 */
public class SearchView extends ViewPart implements SearchResultView {

	/** The ID of the view as specified by the extension. */
	public static final String ID = SearchView.class.getName();

	/** The viewer where search results are displayed. */
	private TreeViewer viewer;

	/**
	 * The label displaying the name of the method that's been searched for he
	 * result currently being displayed on the view.
	 */
	private Label targetInformationLabel;

	private boolean disposed;

	/**
	 * The model holding data associated with the view. Made this static as the
	 * model doesn't change, and therefore continues to hold the same data,
	 * despite the fact that the view can be disposed and re-created.
	 */
	private static SearchResultViewModel VIEW_MODEL = new DefaultSearchViewModel();

	static {
		RangerComponentFactoryImpl.getInstance().getSearchResultViewController().setSearchResultViewModel(VIEW_MODEL);
	}

	public SearchView() {
		RangerComponentFactoryImpl.getInstance().getSearchResultViewController().setSearchResultView(this);
		this.disposed = false;
	}

	/** {@inheritDoc} */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);

		// Setting up and positioning the target information label
		this.targetInformationLabel = new Label(parent, SWT.NONE);
		GridData targetInformationLabelGridData = new GridData();
		targetInformationLabelGridData.horizontalAlignment = GridData.FILL;
		targetInformationLabelGridData.grabExcessHorizontalSpace = true;
		this.targetInformationLabel.setText("No search data found");
		this.targetInformationLabel.setLayoutData(targetInformationLabelGridData);

		// Setting up and positioning the the viewer 
		this.viewer = new SearchViewTreeViewer(parent);
		this.viewer.setContentProvider(VIEW_MODEL);
		this.viewer.setLabelProvider(new SearchViewLabelProvider());
		this.viewer.setSorter(new NameSorter());
		this.viewer.setInput(getViewSite());
		GridData treeViewerGridData = new GridData();
		treeViewerGridData.horizontalAlignment = GridData.FILL;
		treeViewerGridData.verticalAlignment = GridData.FILL;
		treeViewerGridData.grabExcessHorizontalSpace = true;
		treeViewerGridData.grabExcessVerticalSpace = true;
		treeViewerGridData.horizontalIndent = 0;
		this.viewer.getTree().setLayoutData(treeViewerGridData);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.viewer.getControl(), "Ranger.viewer");

		// Hook all needed actions to the view GUI
		this.hookActionsToViewToolbar();
		this.hookDoubleClickAction();
		this.hookActionsToLocalPullDownMenu();

		try {
			// If all goes well, perform an update for any search results that may
			// have been received when the view wasn't visible or created
			// yet.
			this.updateResults();
		} catch (SearchViewException e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		this.disposed = true;
		super.dispose();
	}

	/** {@inheritDoc} */
	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	/** {@inheritDoc} */
	@Override
	public void updateResults() throws SearchViewException {
		if (this.disposed) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID);
			} catch (PartInitException e) {
				throw new SearchViewException("Error updating the view", e);
			}
		} else {
			SearchResult currentResult = VIEW_MODEL.getSearchResult();
			if (currentResult != null) {
				this.targetInformationLabel.setText("Results for \'" + currentResult.getTarget().getName() + "\' - " + currentResult.getTargetsFound().size() + " tests found");
				this.viewer.refresh();
				this.getViewSite().getPage().activate(this);
			}
		}
	}

	// ---------------------------
	// PRIVATE/UTILITY METHODS/CLASSES
	// ---------------------------

	/** Hook actions and respective buttons to the view's tool bar. */
	private void hookActionsToViewToolbar() {
		IToolBarManager manager = this.getViewSite().getActionBars().getToolBarManager();
		manager.add(new DefaultGroupingAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance()));
		manager.add(new GroupByProjectAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance()));
		manager.add(new GroupByClassAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance()));
		manager.add(new Separator());
		manager.add(new CollapseAllAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance(), this.viewer));
		manager.add(new ExpandAllAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance(), this.viewer));
		manager.add(new Separator());
		manager.add(new StopSearchAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance()));
	}

	/** Hooks actions to double clicking on the view. */
	private void hookDoubleClickAction() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = SearchView.this.viewer.getSelection();
				Object selectedElement = ((IStructuredSelection) selection).getFirstElement();

				if (selectedElement instanceof SearchViewNode) {
					OpenInEditorAction action = new OpenInEditorAction(SearchView.this.viewer.getControl().getShell(), (SearchViewNode) selectedElement, RangerComponentFactoryImpl.getInstance());
					action.run();
				}
			}
		});
	}

	private void hookActionsToLocalPullDownMenu() {
		IMenuManager menuManager = this.getViewSite().getActionBars().getMenuManager();
		menuManager.add(new ChangeSearchDepthAction(this.viewer.getControl().getShell(), RangerComponentFactoryImpl.getInstance()));
	}

	class NameSorter extends ViewerSorter {
	}

	//	private void contributeToActionBars() {
	//		IActionBars bars = getViewSite().getActionBars();
	//		fillLocalPullDown(bars.getMenuManager());
	//	}
	//	private void fillLocalPullDown(IMenuManager manager) {
	//		manager.add(action1);
	//		manager.add(new Separator());
	//	}
	//	private void fillContextMenu(IMenuManager manager) {
	//		manager.add(action1);
	//		// Other plug-ins can contribute there actions here
	//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	//	}
	//	private void hookContextMenu() {
	//		MenuManager menuMgr = new MenuManager("#PopupMenu");
	//		menuMgr.setRemoveAllWhenShown(true);
	//		menuMgr.addMenuListener(new IMenuListener() {
	//			public void menuAboutToShow(IMenuManager manager) {
	//				SearchView.this.fillContextMenu(manager);
	//			}
	//		});
	//		Menu menu = menuMgr.createContextMenu(viewer.getControl());
	//		viewer.getControl().setMenu(menu);
	//		getSite().registerContextMenu(menuMgr, viewer);
	//	}
}