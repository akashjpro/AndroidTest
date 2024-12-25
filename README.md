
#  AndroidTest: List and Detail Screens with MVVM

## 1. Cấu trúc Dự Án

Dự án này được phát triển bằng **Android Native** (Java hoặc Kotlin), với mô hình **MVVM** (Model-View-ViewModel) để quản lý logic và dữ liệu dễ dàng.

### Các thành phần chính:
- **Model**: Quản lý dữ liệu (SampleData, Repository).
- **View**: Hiển thị giao diện người dùng (UI).
- **ViewModel**: Quản lý dữ liệu và logic giữa Model và View.

### Tạo hai màn hình chính:
- **Màn hình danh sách (ListFragment)**: Hiển thị danh sách các mục và các tùy chọn sắp xếp.
- **Màn hình chi tiết (DetailFragment)**: Hiển thị thông tin chi tiết của mỗi mục và cho phép xóa mục.

## 2. Quy trình thực hiện

### Bước 1: Chuẩn bị dữ liệu
- **File `sample_data_list.json`** chứa dữ liệu mẫu:

```json
[
  {
    "index": 44,
    "title": "Ethics in Technology",
    "date": "2023-07-20",
    "description": "Explore the ethical considerations in technology and their implications."
  },
  {
    "index": 29,
    "title": "Content Management Systems Explained",
    "date": "2022-04-25",
    "description": "An overview of popular content management systems and their use cases."
  },
  {
    "index": 38,
    "title": "Understanding Cryptocurrency",
    "date": "2023-01-22",
    "description": "Learn the basics of cryptocurrency and how it works."
  },
  ...
]
```

## 2. Code các màn hình và class 

- **File `Item Class`** :

```data class Item(
    val index: Int,
    val title: String,
    val description: String,
    val date: String
)
```

- **File `TypeSort Class`** :

```enum class TypeSort(val index: Int) {
    INDEX(0),
    TITLE(1),
    DATE(2);
}
```

- **File `AppViewModel Class`** :

```class AppViewModel : ViewModel() {
    // Backing property for the item list, exposed as LiveData
    private val _itemList = MutableLiveData<List<Item>>(emptyList())
    val itemList: LiveData<List<Item>> get() = _itemList

    // Backing property for the selected item, exposed as LiveData
    private val _itemSelected = MutableLiveData<Item?>()
    val itemSelected: LiveData<Item?> get() = _itemSelected

    // Update the list of items
    fun setItemList(items: List<Item>) {
        _itemList.value = items
    }

    /**
     * Removes the specified item from the list and updates the LiveData.
     *
     * @param item The item to be removed from the list.
     */
    fun removeItem(item: Item) {
        _itemList.value = _itemList.value?.filter { it != item }
    }

    // Set the selected item
    fun setItemSelected(item: Item) {
        _itemSelected.value = item
    }

    // Sort the item list based on criteria
    fun sortData(type: TypeSort) {
        val sortedList = _itemList.value?.let { currentList ->
            when (type) {
                TypeSort.INDEX -> currentList.sortedByDescending { it.index }
                TypeSort.TITLE -> currentList.sortedByDescending { it.title }
                TypeSort.DATE -> currentList.sortedByDescending { it.date }
            }
        } ?: emptyList()

        // Update the sorted list
        _itemList.value = sortedList
    }
}
```

- **File `Acctivity main`** :

```class MainActivity : AppCompatActivity() {

    // Binding for the activity's layout
    private lateinit var binding: ActivityMainBinding

    // Configuration for the app bar and navigation
    private lateinit var appBarConfiguration: AppBarConfiguration

    // ViewModel scoped to this Activity
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        loadSampleData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**
     * Loads sample data from a JSON file in the assets folder and updates the ViewModel's item list.
     *
     * - Reads the JSON file "sample_data_list.json" from the assets directory.
     * - Parses the JSON data into an array of `Item` objects using Gson.
     * - Converts the array to a list and sets it in the ViewModel.
     */
    private fun loadSampleData() {
        val json = assets.open("sample_data_list.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val items = gson.fromJson(json, Array<Item>::class.java)

        viewModel.setItemList(items.toList())
    }
}
```

- **File `ItemAdapter Class`** :

```class ItemAdapter(
    private val items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val date: TextView = view.findViewById(R.id.date)

        fun bind(item: Item) {
            title.text = item.title
            date.text = item.date
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
```


- **File `List Fragment`** :

```class ListFragment : Fragment() {

    // Binding for the fragment
    private var _binding: FragmentListBinding? = null

    // Only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    // ViewModel obtained from the Activity
    private val viewModel: AppViewModel by activityViewModels()

    // Adapter for the RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemList.observe(viewLifecycleOwner) { items ->
            adapter = ItemAdapter(items) { selectedItem ->
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                viewModel.setItemSelected(item = selectedItem)
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter

            adapter.notifyDataSetChanged()
        }

        // Set up Spinner (sort options)
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortList(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    // Sort the list based on selected option in Spinner
    @SuppressLint("NotifyDataSetChanged")
    private fun sortList(position: Int) {
        when (position) {
            0 -> viewModel.sortData(TypeSort.INDEX) // Sort by Index DESC
            1 -> viewModel.sortData(TypeSort.TITLE) // Sort by Title DESC
            2 -> viewModel.sortData(TypeSort.DATE)  // Sort by Date DESC
        }
        adapter.notifyDataSetChanged()


//        TypeSort.entries.find { it.index == position }?.let {
//            viewModel.sortData(it)
//        }
//
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

- **File `Detail Fragment`** :

```class DetailFragment : Fragment() {

    // Binding for the fragment (only valid between onCreateView and onDestroyView)
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel obtained from the Activity
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = viewModel.itemSelected.value

        binding.title.text = item?.title
        binding.desc.text = item?.description
        binding.date.text = item?.date

        item?.let {
            binding.deleteButton.setOnClickListener {
                showDeleteConfirmationDialog(item) {
                    viewModel.removeItem(item)
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                }
            }
        }

    }

    /**
     * Displays a confirmation dialog to delete the specified item.
     *
     * @param item The item to be deleted.
     * @param onConfirm Callback executed if the user confirms the deletion.
     */
    private fun showDeleteConfirmationDialog(item: Item, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.title} ?")
            .setPositiveButton("Delete") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

<img src="assets/screen1.png" alt="Alt text" width="300">

<img src="assets/screen2.png" alt="Alt text" width="300">